package br.jus.trf2.balcaojus;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoValidarGet;
import br.jus.trf2.balcaojus.IBalcaojus.ProcessoValido;
import br.jus.trf2.balcaojus.util.AcessoPublicoEPrivado;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameProcessoNumerosGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Processo;

@AcessoPublicoEPrivado
public class ProcessoValidarGet implements IProcessoValidarGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		boolean fPorCaptcha = false;
		if (req.captcha != null) {
			if (!Utils.verifyCaptcha(req.captcha))
				throw new PresentableUnloggedException("Token de reCaptcha inválido");
			fPorCaptcha = true;
		} else if (isValidToken(req.token, req.numero)) {
			resp.token = req.token;
		} else
			AutenticarPost.assertAuthorization();

		String usuario = null;
		try {
			Usuario u = AutenticarPost.assertUsuario();
			usuario = u.usuario;
		} catch (Exception e) {
			usuario = BalcaojusServlet.INSTANCE.getProperty("public.username");
		}

		String[] numeros = (req.numero != null && req.numero.trim() != "") ? req.numero.split(",") : null;
		if (numeros != null && numeros.length > 100)
			throw new PresentableException(
					"Não é permitido validar mais de 100 números de processos em uma única operação");

		validar(usuario, numeros, req.nome, req.tipodedocumento, req.documento,req.oab, resp);
		if (fPorCaptcha && resp.list != null && resp.list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (ProcessoValido p : resp.list) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(p.numero);
			}
			resp.token = jwt(sb.toString());
		}
	}

	public static void validar(String usuario, String[] numeros, IProcessoValidarGet.Response resp)
			throws Exception, PresentableException {
		validar(usuario, numeros, null, null, null,null, resp);
	}

	public static void validar(String usuario, String[] numeros, String nome, String tipoDeDocumento, String documento,String oab, 
			IProcessoValidarGet.Response resp) throws Exception, PresentableException {
		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		int timeout;
		String url;
		String docvalido;
		String context;
		
		
		if (documento != null)
			
		{  
			docvalido = documento.replaceAll("[^0-9]", "");
			if (docvalido.isEmpty())
				throw new PresentableException(
						"Termo de busca inválido !! " + documento);
			
			url = "/processo/consultar?documento=" + URLEncoder.encode(docvalido, "UTF-8").replace("+", "%20");
			context = " - consulta processo por cpf/cnpj da parte";
			timeout = 30000;
		}
		else if (nome != null)
		{
			url = "/processo/consultar?nomeparte="
					+ URLEncoder.encode(nome.toUpperCase(), "UTF-8").replace("+", "%20");
			context = " - consulta processo por nome da parte";
			timeout = 30000;
		}
		else if (oab != null)
		{
			url = "/processo/consultar?oab="
					+ URLEncoder.encode(oab.toUpperCase(), "UTF-8").replace("+", "%20");
			context = " - consulta processo por oab";
			timeout = 30000;
		}
		else {
			IUsuarioUsernameProcessoNumerosGet.Request q = new IUsuarioUsernameProcessoNumerosGet.Request();
			q.numeros = StringUtils.join(numeros, ",");
			url = "/processo/" + q.numeros;
			context = " - validar número de processo";
			timeout = 15000;
		}
		
		url = url.replace("_", "%20");
		
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + context, Utils.getApiPassword(system),
							"GET", Utils.getApiUrl(system) + "/usuario/" + usuario + url, null,
							IUsuarioUsernameProcessoNumerosGet.Response.class));
		}
		
		
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, timeout);
		resp.status = Utils.getStatus(mcr);
		//resp.list = new ArrayList<>();

		Map<String, ProcessoValido> processosRecebidos = new HashMap<>();
		ProcessoValido proc;

		// TODO: Falta lógica para escolher o mais importante dos resultados.
		for (String system : mcr.responses.keySet()) {
			IUsuarioUsernameProcessoNumerosGet.Response rl = (IUsuarioUsernameProcessoNumerosGet.Response) mcr.responses
					.get(system);
			if (rl.list == null || rl.list.size() == 0)
				continue;
			for (Processo r : rl.list) {
				if (r.numero == null || r.perdecompetencia)
					continue;
				if (processosRecebidos.containsKey(r.numero))
				{
					//considera o processo no Eproc ao decidir competência
					proc = processosRecebidos.get(r.numero);  
					if (proc.sistema.contains("apolo") && system.contains("eproc")) {
						processosRecebidos.remove(r.numero);
					}
					else
						if (proc.sistema.contains("eproc") && system.contains("apolo"))
						continue;
					
					else
						throw new PresentableException(
							"Não foi possível identificar qual sistema tem competência para o processo: " + r.numero,
							mcr.status);
				}
							
				
				ProcessoValido pv = new ProcessoValido();
				pv.numero = r.numero;
				pv.sistema = system;
				pv.orgao = r.orgao;
				pv.unidade = r.unidade != null ? r.unidade.trim() : null;
				pv.localnaunidade = r.localNaUnidade;
				pv.segredodejustica = r.segredodejustica;
				pv.segredodejusticadesistema = r.segredodejusticadesistema;
				pv.segredodejusticaabsoluto = r.segredodejusticaabsoluto;
				pv.usuarioautorizado = r.usuarioautorizado;
				pv.digital = r.eletronico;
				pv.sentenciado = r.sentenciado;
				pv.baixado = r.baixado;
				pv.cdas = r.cdas;
				if (r.dataultimomovimento != null)
					pv.dataultimomovimento = Utils.parsearDataHoraMinuto(r.dataultimomovimento);
				pv.autor = Texto.maiusculasEMinusculas(r.autor);
				pv.reu = Texto.maiusculasEMinusculas(r.reu);
				processosRecebidos.put(pv.numero,pv);
			}
		}
		resp.list.addAll(processosRecebidos.values());
		resp.datavalidacao = new Date();
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getApiPassword());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	public static String jwt(String processo) throws Exception {
		final String issuer = Utils.getJwtIssuer();
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		// token expires in 12h
		final long exp = iat + 12 * 60 * 60L;

		final JWTSigner signer = new JWTSigner(Utils.getApiPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("proc", processo);

		claims.put("typ", "consulta-publica");

		final String jwt = signer.sign(claims);
		return jwt;
	}

	public static boolean isValidToken(String token, String processo) throws PresentableUnloggedException {
		if (token == null || processo == null)
			return false;
		Map<String, Object> m;
		try {
			m = verify(token);
		} catch (Exception ex) {
			throw new PresentableUnloggedException("Token de consulta pública inválido inválido", ex);
		}
		String[] numeros = ((String) m.get("proc")).split(",");
		if (!Arrays.asList(numeros).contains(processo))
			throw new PresentableUnloggedException("Token de consulta pública com número de processo inválido");
		return true;
	}

}

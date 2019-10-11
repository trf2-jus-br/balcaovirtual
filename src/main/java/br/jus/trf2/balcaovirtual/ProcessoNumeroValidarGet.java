package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroValidarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoValido;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Processo;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameProcessoNumerosGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameProcessoNumerosGetResponse;

public class ProcessoNumeroValidarGet implements IProcessoNumeroValidarGet {

	@Override
	public void run(ProcessoNumeroValidarGetRequest req, ProcessoNumeroValidarGetResponse resp) throws Exception {
		if (req.captcha != null) {
			if (!Utils.verifyCaptcha(req.captcha))
				throw new PresentableUnloggedException("Token de reCaptcha inválido");
			resp.token = jwt(req.numero);
		} else if (isValidToken(req.token, req.numero)) {
			resp.token = req.token;
		} else
			SessionsCreatePost.assertAuthorization();

		String usuario = null;
		try {
			Usuario u = SessionsCreatePost.assertUsuario();
			usuario = u.usuario;
		} catch (Exception e) {
			usuario = SwaggerServlet.getProperty("public.username");
		}

		String[] numeros = req.numero.split(",");
		if (numeros.length > 100)
			throw new PresentableException(
					"Não é permitido validar mais de 100 números de processos em uma única operação");

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			UsuarioUsernameProcessoNumerosGetRequest q = new UsuarioUsernameProcessoNumerosGetRequest();
			q.numeros = req.numero;
			mapp.put(system,
					new SwaggerCallParameters(system + " - validar número de processo", Utils.getApiPassword(system),
							"GET", Utils.getApiUrl(system) + "/usuario/" + usuario + "/processo/" + req.numero, null,
							UsuarioUsernameProcessoNumerosGetResponse.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, 15000);
		resp.status = Utils.getStatus(mcr);
		resp.list = new ArrayList<>();

		Set<String> numerosRecebidos = new HashSet<>();

		// TODO: Falta lógica para escolher o mais importante dos resultados.
		for (String system : mcr.responses.keySet()) {
			UsuarioUsernameProcessoNumerosGetResponse rl = (UsuarioUsernameProcessoNumerosGetResponse) mcr.responses
					.get(system);
			if (rl.list == null || rl.list.size() == 0)
				continue;
			for (Processo r : rl.list) {
				if (r.numero == null || r.perdecompetencia)
					continue;
				if (numerosRecebidos.contains(r.numero))
					throw new PresentableException(
							"Não foi possível identificar qual sistema tem competência para o processo: " + r.numero,
							mcr.status);
				numerosRecebidos.add(r.numero);
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
				resp.list.add(pv);
			}
		}
		resp.datavalidacao = new Date();
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getJwtSecret());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	public static String jwt(String processo) throws Exception {
		final String issuer = Utils.getJwtIssuer();
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		// token expires in 12h
		final long exp = iat + 12 * 60 * 60L;

		final JWTSigner signer = new JWTSigner(Utils.getJwtSecret());
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
		if (!m.get("proc").equals(processo))
			throw new PresentableUnloggedException("Token de consulta pública com número de processo inválido");
		return true;
	}

}

package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroValidarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetResponse;

public class ProcessoNumeroValidarGet implements IProcessoNumeroValidarGet {

	@Override
	public void run(ProcessoNumeroValidarGetRequest req, ProcessoNumeroValidarGetResponse resp) throws Exception {
		if (req.captcha != null) {
			if (!Utils.verifyCaptcha(req.captcha))
				throw new PresentableUnloggedException("Token de reCaptcha inválido");
			resp.token = jwt(req.numero);
		} else if (assertValidToken(req.token, req.numero)) {
			resp.token = req.token;
		} else
			SessionsCreatePost.assertAuthorization();

		String url = null;
		try {
			Usuario u = SessionsCreatePost.assertUsuario();
			url = "/usuario/" + u.usuario + "/processo/" + req.numero;
		} catch (Exception e) {
			url = "/processo/validar/" + req.numero;
		}

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			ProcessoValidarNumeroGetRequest q = new ProcessoValidarNumeroGetRequest();
			q.numero = req.numero;
			mapp.put(system, new SwaggerCallParameters(system + " - validar número de processo", null, "GET",
					Utils.getApiUrl(system) + url, null, ProcessoValidarNumeroGetResponse.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, 15000);

		// TODO: Falta lógica para escolher o mais importante dos resultados.
		for (String system : mcr.responses.keySet()) {
			ProcessoValidarNumeroGetResponse r = (ProcessoValidarNumeroGetResponse) mcr.responses.get(system);
			if (r.numero == null || r.perdecompetencia)
				continue;
			if (resp.numero != null)
				throw new PresentableException(
						"Não foi possível identificar qual sistema tem competência para o processo: " + resp.numero);
			resp.numero = r.numero;
			resp.sistema = system;
			resp.orgao = r.orgao;
			resp.unidade = r.unidade != null ? r.unidade.trim() : null;
			resp.localnaunidade = r.localNaUnidade;
			resp.segredodejustica = r.segredodejustica;
			resp.segredodejusticadesistema = r.segredodejusticadesistema;
			resp.segredodejusticaabsoluto = r.segredodejusticaabsoluto;
			resp.usuarioautorizado = r.usuarioautorizado;
			resp.digital = r.eletronico;
			resp.sentenciado = r.sentenciado;
			resp.baixado = r.baixado;
			resp.cdas = r.cdas;
			if (r.dataultimomovimento != null)
				resp.dataultimomovimento = Utils.parsearDataHoraMinuto(r.dataultimomovimento);
		}
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

	public static boolean assertValidToken(String token, String processo) throws PresentableUnloggedException {
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

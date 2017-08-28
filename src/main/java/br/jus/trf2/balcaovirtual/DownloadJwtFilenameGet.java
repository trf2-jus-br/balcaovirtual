package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IDownloadJwtFilenameGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoNumeroPdfGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoNumeroPdfGetResponse;

public class DownloadJwtFilenameGet implements IDownloadJwtFilenameGet {
	private static final Logger log = LoggerFactory.getLogger(DownloadJwtFilenameGet.class);

	@Override
	public void run(DownloadJwtFilenameGetRequest req, DownloadJwtFilenameGetResponse resp) throws Exception {
		Map<String, Object> map = verify(req.jwt);
		String numProc = (String) map.get("proc");
		String numDoc = (String) map.get("doc");
		String orgao = (String) map.get("orgao");
		String type = (String) map.get("typ");
		String disposition = "attachment".equals(req.disposition) ? "attachment" : "inline";
		if (!"download".equals(type))
			throw new Exception("Tipo de token JWT inválido");
		if (numDoc != null) {
			// Peça Processual
			byte[] ab = SoapMNI.obterPecaProcessual(req.jwt, orgao, numProc, numDoc);
			resp.contentdisposition = disposition + ";filename=" + numProc + "-peca-" + numDoc + ".pdf";
			resp.contentlength = (long) ab.length;
			resp.inputstream = new ByteArrayInputStream(ab);
		} else {
			// Processo completo
			ProcessoNumeroPdfGetRequest q = new ProcessoNumeroPdfGetRequest();
			Future<SwaggerAsyncResponse<ProcessoNumeroPdfGetResponse>> future = SwaggerCall.callAsync(
					"obter PDF completo de processo", "Bearer " + req.jwt, "GET",
					Utils.getWsProcessualUrl() + "/processo/" + numProc + "/pdf", null,
					ProcessoNumeroPdfGetResponse.class);
			SwaggerAsyncResponse<ProcessoNumeroPdfGetResponse> sar = future.get();
			if (sar.getException() != null)
				throw sar.getException();
			ProcessoNumeroPdfGetResponse r = (ProcessoNumeroPdfGetResponse) sar.getResp();
			resp.contentdisposition = disposition + ";filename=" + numProc + "-completo.pdf";
			resp.contentlength = r.contentlength;
			resp.contenttype = r.contenttype;
			resp.inputstream = r.inputstream;
		}
	}

	@Override
	public String getContext() {
		return "obter arquivo";
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getJwtSecret());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	public static String jwt(String origin, String username, String orgao, String processo, String documento) {
		final String issuer = Utils.getJwtIssuer();
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		// token expires in 10min or 12h
		final long exp = iat + (documento != null ? 10 * 60L : 12 * 60 * 60L);

		final JWTSigner signer = new JWTSigner(Utils.getJwtSecret());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);

		if (origin != null)
			claims.put("origin", origin);
		claims.put("username", username);
		claims.put("orgao", orgao);
		claims.put("proc", processo);
		if (documento != null)
			claims.put("doc", documento);
		claims.put("typ", "download");

		final String jwt = signer.sign(claims);
		return jwt;
	}

}

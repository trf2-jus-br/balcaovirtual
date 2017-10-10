package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IDownloadJwtFilenameGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoNumeroPdfGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernameAvisoPendenteExportarGetResponse;

public class DownloadJwtFilenameGet implements IDownloadJwtFilenameGet {

	@Override
	public void run(DownloadJwtFilenameGetRequest req, DownloadJwtFilenameGetResponse resp) throws Exception {
		Map<String, Object> map = verify(req.jwt);
		String file = (String) map.get("file");
		String numProc = (String) map.get("proc");
		String numDoc = (String) map.get("doc");
		String orgao = (String) map.get("orgao");
		String type = (String) map.get("typ");
		String disposition = "attachment".equals(req.disposition) ? "attachment" : "inline";
		if (!"download".equals(type))
			throw new Exception("Tipo de token JWT inválido");
		if (file != null && file.equals("avisos-pendentes.xml")) {
			// Processo completo
			Future<SwaggerAsyncResponse<UsuarioWebUsernameAvisoPendenteExportarGetResponse>> future = SwaggerCall
					.callAsync("obter XML de avisos", "Bearer " + req.jwt, "GET",
							Utils.getWsProcessualUrl() + "/usuario-web/" + map.get("username")
									+ "/aviso-pendente/exportar",
							null, UsuarioWebUsernameAvisoPendenteExportarGetResponse.class);
			SwaggerAsyncResponse<UsuarioWebUsernameAvisoPendenteExportarGetResponse> sar = future.get();
			if (sar.getException() != null)
				throw sar.getException();
			UsuarioWebUsernameAvisoPendenteExportarGetResponse r = (UsuarioWebUsernameAvisoPendenteExportarGetResponse) sar
					.getResp();
			resp.contentdisposition = "attachment;filename=" + map.get("username") + "-avisos-pendentes.xml";
			resp.contentlength = r.contentlength;
			resp.contenttype = r.contenttype;
			resp.inputstream = r.inputstream;
		} else if (numDoc != null) {
			// Peça Processual
			byte[] ab = SoapMNI.obterPecaProcessual(req.jwt, orgao, numProc, numDoc);
			resp.contentdisposition = disposition + ";filename=" + numProc + "-peca-" + numDoc + ".pdf";
			resp.contentlength = (long) ab.length;
			resp.inputstream = new ByteArrayInputStream(ab);
		} else {
			// Processo completo
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

	public static String jwt(String origin, String username, String orgao, String processo, String documento,
			String arquivo) {
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
		if (orgao != null)
			claims.put("orgao", orgao);
		if (processo != null)
			claims.put("proc", processo);
		if (documento != null)
			claims.put("doc", documento);
		if (arquivo != null)
			claims.put("file", arquivo);
		claims.put("typ", "download");

		final String jwt = signer.sign(claims);
		return jwt;
	}

}

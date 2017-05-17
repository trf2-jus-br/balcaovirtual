package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IDownloadJwtFilenameGet;

public class DownloadJwtFilenameGet implements IDownloadJwtFilenameGet {
	private static final Logger log = LoggerFactory.getLogger(DownloadJwtFilenameGet.class);

	@Override
	public void run(DownloadJwtFilenameGetRequest req, DownloadJwtFilenameGetResponse resp) throws Exception {
		Map<String, Object> map = verify(req.jwt);
		String numProc = (String) map.get("proc");
		String numDoc = (String) map.get("doc");
		String orgao = (String) map.get("orgao");
		String type = (String) map.get("typ");
		if (!"download".equals(type))
			throw new Exception("Tipo de token JWT inválido");
		resp.payload = SoapMNI.obterPecaProcessual(req.jwt, orgao, numProc, numDoc);
		resp.contenttype = "application/pdf";
	}

	@Override
	public String getContext() {
		return "obter arquivo temporário";
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getJwtSecret());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	public static String jwt(String username, String orgao, String processo, String documento) {
		final String issuer = Utils.getJwtIssuer();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 2 * 60L; // token expires in 2min

		final JWTSigner signer = new JWTSigner(Utils.getJwtSecret());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);

		claims.put("username", username);
		claims.put("orgao", orgao);
		claims.put("proc", processo);
		claims.put("doc", documento);
		claims.put("typ", "download");

		final String jwt = signer.sign(claims);
		return jwt;
	}

}

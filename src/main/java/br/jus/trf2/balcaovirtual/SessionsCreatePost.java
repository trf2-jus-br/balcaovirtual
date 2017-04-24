package br.jus.trf2.balcaovirtual;

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
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ISessionsCreatePost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SessionsCreatePostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SessionsCreatePostResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.AutenticarPostRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.AutenticarPostResponse;

public class SessionsCreatePost implements ISessionsCreatePost {
	private static final Logger log = LoggerFactory.getLogger(SessionsCreatePost.class);

	@Override
	public void run(SessionsCreatePostRequest req, SessionsCreatePostResponse resp) throws Exception {
		AutenticarPostRequest q = new AutenticarPostRequest();
		q.login = req.username;
		q.senha = req.password;
		Future<SwaggerAsyncResponse<AutenticarPostResponse>> future = SwaggerCall.callAsync("autenticar usuário", null,
				"POST", Utils.getWsProcessualUrl() + "/login/autenticar", q, AutenticarPostResponse.class);
		SwaggerAsyncResponse<AutenticarPostResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		AutenticarPostResponse r = (AutenticarPostResponse) sar.getResp();
		String jwt = jwt(req.username, r.cpf, r.nome, r.email);
		verify(jwt);
		resp.id_token = jwt;
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		if (jwt.startsWith("Bearer "))
			jwt = jwt.substring(7);
		final JWTVerifier verifier = new JWTVerifier(getSecret());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	private static String jwt(String username, String cpf, String name, String email) {
		final String issuer = SwaggerUtils.getProperty("balcaovirtual.jwt.issuer", null);

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 24 * 60 * 60L; // token expires in 1 day

		final JWTSigner signer = new JWTSigner(getSecret());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);

		claims.put("username", username);
		claims.put("cpf", cpf);
		claims.put("name", name);
		claims.put("email", email);

		final String jwt = signer.sign(claims);
		return jwt;
	}

	private static String getSecret() {
		return SwaggerUtils.getProperty("balcaovirtual.jwt.secret", null);
	}

	@Override
	public String getContext() {
		return "autenticar usuário";
	}

}

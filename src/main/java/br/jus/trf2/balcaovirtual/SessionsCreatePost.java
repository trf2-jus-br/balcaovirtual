package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.org.apache.commons.lang3.ArrayUtils;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ISessionsCreatePost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SessionsCreatePostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SessionsCreatePostResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernameGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernameGetResponse;

public class SessionsCreatePost implements ISessionsCreatePost {
	private static final Logger log = LoggerFactory.getLogger(SessionsCreatePost.class);
	public static final long TIMEOUT_MILLISECONDS = 15000;

	@Override
	public void run(SessionsCreatePostRequest req, SessionsCreatePostResponse resp) throws Exception {
		String usuariosRestritos = Utils.getUsuariosRestritos();
		if (usuariosRestritos != null) {
			if (!ArrayUtils.contains(usuariosRestritos.split(","), req.username))
				throw new PresentableUnloggedException("Usuário não autorizado.");
		}

		// Read list from connected systems
		String[] systems = Utils.getSystems();
		if (systems == null)
			return;

		String authorization = "Basic " + SwaggerUtils.base64Encode((req.username + ":" + req.password).getBytes());
		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : systems) {
			String urlsys = Utils.getApiUrl(system);

			UsuarioWebUsernameGetRequest q = new UsuarioWebUsernameGetRequest();
			q.username = req.username;
			mapp.put(system, new SwaggerCallParameters(system + "-autenticar-usuário", authorization, "GET",
					urlsys + "/usuario-web/" + req.username, q, UsuarioWebUsernameGetResponse.class));

		}

		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, TIMEOUT_MILLISECONDS);

		// TODO: Tratar o caso do usuário interno (int) para o Apolo!
//		String origem = "int";
//		UsuarioWebUsernameGetResponse r;
//		try {
//			r = wsAutenticar(origem, req.username, req.password);
//		} catch (Exception ex) {
//			origem = null;
//			try {
//				r = wsAutenticar(origem, req.username, req.password);
//			} catch (Exception ex2) {
//				if (ex.getMessage().contains("unknown path"))
//					throw ex2;
//				else
//					throw ex;
//			}
//		}

		String origem = null;
		String usuarios = null;
		String cpf = null;
		String nome = null;
		String email = null;
		for (String system : mcr.responses.keySet()) {
			UsuarioWebUsernameGetResponse u = (UsuarioWebUsernameGetResponse) mcr.responses.get(system);
			if (u.cpf != null)
				cpf = u.cpf;
			if (u.nome != null)
				nome = u.nome;
			if (u.email != null)
				email = u.email;

			if (usuarios == null)
				usuarios = "";
			else
				usuarios += ";";
			usuarios += system + "," + u.codusu + ","
					+ (u.codunidade != null && !u.codunidade.equals("0") ? u.codunidade : "null") + ","
					+ (u.perfil != null && !u.perfil.equals("") ? u.perfil.toLowerCase() : "null");
		}
		if (usuarios == null)
			throw new Exception("Nenhum usuário localizado na base com esse identificador");
		String jwt = jwt(origem, req.username, req.password, cpf, nome, email, usuarios);
		verify(jwt);
		resp.id_token = jwt;
	}

	private static Map<String, Object> verify(String jwt) throws SwaggerAuthorizationException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getJwtSecret());
		Map<String, Object> map;
		try {
			map = verifier.verify(jwt);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException
				| IOException | JWTVerifyException e) {
			throw new SwaggerAuthorizationException(e);
		}
		return map;
	}

	public static class UsuarioDetalhe {
		String id;
		String unidade;
	}

	public static class Usuario {
		String origem;
		String email;
		String nome;
		String usuario;
		String cpf;
		String senha;

		String orgao;
		String codusu;
		String codusuweb;
		String codunidade;
		String perfil;
		String system;

		Map<String, UsuarioDetalhe> usuarios;

		boolean isInterno() {
			return origem != null && origem.startsWith("int");
		}
	}

	public static Usuario assertUsuario() throws Exception {
		Map<String, Object> jwt = assertUsuarioAutorizado();
		Usuario u = new Usuario();

		u.origem = (String) jwt.get("origin");
		u.email = (String) jwt.get("email");
		u.nome = (String) jwt.get("name");
		u.cpf = (String) jwt.get("cpf");
		u.usuario = (String) jwt.get("username");
		u.senha = SessionsCreatePost.decrypt((String) jwt.get("pwd"));
		String users = (String) jwt.get("users");
		if (users != null && users.length() > 0) {
			u.usuarios = new HashMap<>();
			for (String s : users.split(";")) {
				String[] ss = s.split(",");
				UsuarioDetalhe ud = new UsuarioDetalhe();
				if (!"null".equals(ss[1]))
					ud.id = ss[1];
				if (!"null".equals(ss[2]))
					ud.unidade = ss[2];
				u.usuarios.put(ss[0], ud);
			}
		}
		return u;
	}

	public static Map<String, Object> assertUsuarioAutorizado() throws Exception {
		String authorization = getAuthorizationHeader();
		return verify(authorization);
	}

	private static String getAuthorizationHeader() throws SwaggerAuthorizationException {
		String authorization = BalcaoVirtualServlet.getHttpServletRequest().getHeader("Authorization");
		if (authorization == null)
			throw new SwaggerAuthorizationException("Authorization header is missing");
		if (authorization.startsWith("Bearer "))
			authorization = authorization.substring(7);
		return authorization;
	}

	public static String assertAuthorization() throws SwaggerAuthorizationException {
		String authorization = getAuthorizationHeader();
		verify(authorization);
		return authorization;
	}

	private static String jwt(String origin, String username, String password, String cpf, String name, String email,
			String users) throws Exception {
		final String issuer = Utils.getJwtIssuer();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 18 * 60 * 60L; // token expires in 18 hours

		final JWTSigner signer = new JWTSigner(Utils.getJwtSecret());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);

		if (origin != null)
			claims.put("origin", origin);
		claims.put("username", username);
		claims.put("pwd", encrypt(password));
		claims.put("cpf", cpf);
		claims.put("name", name);
		claims.put("email", email);
		claims.put("users", users);

		final String jwt = signer.sign(claims);
		return jwt;
	}

	public static String encrypt(String text) throws Exception {
		String key = Utils.getJwtSecret().substring(0, 16); // 128 bit key
		// Create key and cipher
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		// encrypt the text
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(text.getBytes());
		return SwaggerUtils.base64Encode(encrypted);
	}

	public static String decrypt(String text) throws Exception {
		byte[] ab = SwaggerUtils.base64Decode(text);
		String key = Utils.getJwtSecret().substring(0, 16); // 128 bit key
		// Create key and cipher
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		// decrypt the text
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		return new String(cipher.doFinal(ab));
	}

	@Override
	public String getContext() {
		return "autenticar usuário";
	}

}

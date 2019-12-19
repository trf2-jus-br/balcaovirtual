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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.org.apache.commons.lang3.ArrayUtils;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AutenticarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AutenticarPostResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAutenticarPost;
import br.jus.trf2.balcaovirtual.util.AcessoPublico;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameGetResponse;

@AcessoPublico
public class AutenticarPost implements IAutenticarPost {
	public static final long TIMEOUT_MILLISECONDS = 15000;
	public static final String JWT_AUTH_COOKIE_NAME = SwaggerServlet.getProperty("cookie.name");
	public static final String JWT_AUTH_COOKIE_DOMAIN = SwaggerServlet.getProperty("cookie.domain");
	// Se 8h é a duração, informar 60 * 60 * 8
	public static final int JWT_AUTH_COOKIE_TIME_TO_EXPIRE_IN_S = Integer
			.valueOf(SwaggerServlet.getProperty("cookie.expire.seconds"));
	// renova automaticamente X segundos antes de expirar
	public static final int JWT_AUTH_COOKIE_TIME_TO_RENEW_IN_S = Integer
			.valueOf(SwaggerServlet.getProperty("cookie.renew.seconds"));

	@Override
	public void run(AutenticarPostRequest req, AutenticarPostResponse resp) throws Exception {
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

			UsuarioUsernameGetRequest q = new UsuarioUsernameGetRequest();
			q.username = req.username;
			mapp.put(system, new SwaggerCallParameters(system + "-autenticar-usuário", authorization, "GET",
					urlsys + "/usuario/" + req.username, q, UsuarioUsernameGetResponse.class));

		}

		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, TIMEOUT_MILLISECONDS);
		resp.status = Utils.getStatus(mcr);

		String origem = null;
		String usuarios = null;
		String cpf = null;
		String nome = null;
		String email = null;
		for (String system : mcr.responses.keySet()) {
			UsuarioUsernameGetResponse u = (UsuarioUsernameGetResponse) mcr.responses.get(system);
			if (u.codusu == null)
				continue;
			if (origem == null)
				origem = u.interno ? "int" : "ext";
			else if ((origem.equals("int") && !u.interno) || (origem.equals("ext") && u.interno))
				origem = "int/ext";
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
			usuarios += system + "," + u.codusu + "," + (u.interno ? "int" : "ext") + ","
					+ serialize(u.codentidade != null && !u.codentidade.equals("0") ? u.codentidade : null) + ","
					+ serialize(u.entidade) + ","
					+ serialize(u.codunidade != null && !u.codunidade.equals("0") ? u.codunidade : null) + ","
					+ serialize(u.unidade) + ","
					+ (u.perfil != null && !u.perfil.equals("") ? u.perfil.toLowerCase() : "null");
		}

		if (usuarios == null)
			throw new SwaggerAuthorizationException("Credenciais rejeitadas. Base" + (systems.length == 1 ? "" : "s")
					+ " acessada" + (systems.length == 1 ? "" : "s") + ": " + Utils.getSystemsNames() + ".",
					mcr.status);
		String jwt = jwt(origem, req.username, cpf, nome, email, usuarios);
		verify(jwt);
		resp.id_token = jwt;

		Cookie cookie = buildCookie(jwt);
		SwaggerServlet.getHttpServletResponse().addCookie(cookie);

		Usuario.setSenha(req.username, req.password);
	}

	public static class UsuarioDetalhe {
		String id;
		String origem;
		String codentidade;
		String entidade;
		String codunidade;
		String unidade;
		String perfil;
	}

	public static class Usuario {
		final static String USUARIO_PREFIXO_SENHA = "bv-pwd-";
		String origem;
		String email;
		String nome;
		String usuario;
		String cpf;

		Map<String, UsuarioDetalhe> usuarios;
		public String stringDeUsuarios;

		boolean isInterno() {
			return origem != null && origem.startsWith("int");
		}

		String getSenha() throws Exception {
			return getSenha(usuario);
		}

		public static void setSenha(String usuario, String senha) throws Exception {
			SwaggerUtils.memCacheStore(USUARIO_PREFIXO_SENHA + usuario.toLowerCase(), encrypt(senha).getBytes());
		}

		public static String getSenha(String usuario) throws Exception {
			byte[] ab = SwaggerUtils.memCacheRetrieve(USUARIO_PREFIXO_SENHA + usuario.toLowerCase());
			if (ab == null)
				throw new SwaggerAuthorizationException();
			return decrypt(new String(ab));
		}
	}

	public static Usuario assertUsuario() throws Exception {
		Map<String, Object> jwt = assertUsuarioAutorizado();
		Usuario u = usuarioFromJwtMap(jwt);
		return u;
	}

	public static Usuario usuarioFromJwtMap(Map<String, Object> jwt) throws Exception {
		Usuario u = new Usuario();

		u.origem = (String) jwt.get("origin");
		u.email = (String) jwt.get("email");
		u.nome = (String) jwt.get("name");
		u.cpf = (String) jwt.get("cpf");
		u.usuario = (String) jwt.get("username");
		String users = (String) jwt.get("users");
		u.stringDeUsuarios = users;
		if (users != null && users.length() > 0) {
			u.usuarios = new HashMap<>();
			for (String s : users.split(";")) {
				String[] ss = s.split(",");
				UsuarioDetalhe ud = new UsuarioDetalhe();
				ud.id = parse(ss[1]);
				ud.origem = parse(ss[2]);
				ud.codentidade = parse(ss[3]);
				ud.entidade = parse(ss[4]);
				ud.codunidade = parse(ss[5]);
				ud.codunidade = parse(ss[6]);
				ud.perfil = parse(ss[7]);
				u.usuarios.put(ss[0], ud);
			}
		}
		return u;
	}

	private static String parse(String s) {
		if ("null".equals(s))
			return null;
		return s;
	}

	private static String serialize(String s) {
		if (s == null || s.trim().length() == 0)
			return "null";
		return s.replace(",", ".").replace(";", ".Fs");
	}

	public static Map<String, Object> assertUsuarioAutorizado() throws Exception {
		String authorization = getAuthorizationHeader();
		return verify(authorization);
	}

	public static String assertAuthorization() throws SwaggerAuthorizationException {
		String authorization = getAuthorizationHeader();
		verify(authorization);
		return authorization;
	}

	public static String getAuthorizationHeader() throws SwaggerAuthorizationException {
		String authorization = BalcaoVirtualServlet.getHttpServletRequest().getHeader("Authorization");

		if (authorization == null) {
			Cookie[] cookies = SwaggerServlet.getHttpServletRequest().getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if (JWT_AUTH_COOKIE_NAME.equals(c.getName()))
						authorization = c.getValue();
				}
			}
		}

		if (authorization == null)
			throw new SwaggerAuthorizationException("Authorization header or cookie is missing");
		if (authorization.startsWith("Bearer "))
			authorization = authorization.substring(7);
		return authorization;
	}

	public static Map<String, Object> verify(String jwt) throws SwaggerAuthorizationException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getJwtPassword());
		Map<String, Object> map;
		try {
			map = verifier.verify(jwt);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException
				| IOException | JWTVerifyException e) {
			throw new SwaggerAuthorizationException(e);
		}
		return map;
	}

	public static String renew() throws Exception {
		Usuario u = assertUsuario();
		return jwt(u.origem, u.usuario, u.cpf, u.nome, u.email, u.stringDeUsuarios);
	}

	private static String jwt(String origin, String username, String cpf, String name, String email, String users)
			throws Exception {
		final String issuer = Utils.getJwtIssuer();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + JWT_AUTH_COOKIE_TIME_TO_EXPIRE_IN_S; // token expires in 18 hours

		final JWTSigner signer = new JWTSigner(Utils.getJwtPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);

		if (origin != null)
			claims.put("origin", origin);
		claims.put("username", username);
		claims.put("cpf", cpf);
		claims.put("name", name);
		claims.put("email", email);
		claims.put("users", users);

		final String jwt = signer.sign(claims);
		return jwt;
	}

	public static Cookie buildCookie(String tokenNew) {
		Cookie cookie = new Cookie(JWT_AUTH_COOKIE_NAME, tokenNew);
		cookie.setPath("/");
		if (JWT_AUTH_COOKIE_DOMAIN != null)
			cookie.setDomain(JWT_AUTH_COOKIE_DOMAIN);

		cookie.setMaxAge(JWT_AUTH_COOKIE_TIME_TO_EXPIRE_IN_S);

		// cookie.setSecure(true);
		return cookie;
	}

	public static Cookie buildEraseCookie() {
		Cookie cookie = new Cookie(JWT_AUTH_COOKIE_NAME, "");
		cookie.setPath("/");
		if (JWT_AUTH_COOKIE_DOMAIN != null)
			cookie.setDomain(JWT_AUTH_COOKIE_DOMAIN);

		cookie.setMaxAge(0);
		return cookie;
	}

	public static void informarNaoAutenticado(HttpServletResponse resp, Exception e) throws IOException {
		String mensagem = "Não foi possível autenticar o usuário, se não quiser perder o trabalho, use uma outra janela do navegador para entrar no sistema e fazer um novo login, depois volte nessa página e clique no botão de atualizar: ";
		if (e.getCause() == null)
			mensagem += e.getLocalizedMessage();
		else
			mensagem += e.getCause().getLocalizedMessage();
		resp.setStatus(401); // 401 Unauthorized - authentication is required
								// and has failed or has not yet been provided.
		resp.getWriter().write(mensagem);
	}

	public static void informarProibido(HttpServletResponse resp, Exception e) throws IOException {
		String mensagem = e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getLocalizedMessage();
		resp.setStatus(403); // 403 Forbidden - The request was valid, but the
								// server is refusing action. The user might not
								// have the necessary permissions for a
								// resource, or may need an account of some sort
		resp.getWriter().write(mensagem);
		return;
	}

	public static String encrypt(String text) throws Exception {
		String key = Utils.getJwtPassword().substring(0, 16); // 128 bit key
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
		String key = Utils.getJwtPassword().substring(0, 16); // 128 bit key
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

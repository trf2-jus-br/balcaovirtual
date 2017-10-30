package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.org.apache.commons.lang3.ArrayUtils;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ISessionsCreatePost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SessionsCreatePostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SessionsCreatePostResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernameGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernameGetResponse;

public class SessionsCreatePost implements ISessionsCreatePost {
	@Override
	public void run(SessionsCreatePostRequest req, SessionsCreatePostResponse resp) throws Exception {

		String usuariosRestritos = Utils.getUsuariosRestritos();
		if (usuariosRestritos != null) {
			if (!ArrayUtils.contains(usuariosRestritos.split(","), req.username))
				throw new PresentableUnloggedException("Usuário não autorizado.");
		}

		String origem = "int";
		UsuarioWebUsernameGetResponse r;
		try {
			r = wsAutenticar(origem, req.username, req.password);
		} catch (Exception ex) {
			origem = null;
			try {
				r = wsAutenticar(origem, req.username, req.password);
			} catch (Exception ex2) {
				throw ex;
			}
		}

		String usuarios = null;
		if (r.usuarios != null && r.usuarios.size() > 0) {
			for (ISistemaProcessual.Usuario u : r.usuarios) {
				if (usuarios == null)
					usuarios = "";
				else
					usuarios += ";";
				usuarios += u.orgao.toLowerCase() + "," + u.codusu + ","
						+ (u.codunidade != null && !u.codunidade.equals("0") ? u.codunidade : "null") + ","
						+ (u.perfil != null && !u.perfil.equals("") ? u.perfil.toLowerCase() : "null");
			}
		}

		String jwt = jwt(origem, req.username, r.cpf, r.nome, r.email, usuarios);
		verify(jwt);
		resp.id_token = jwt;
	}

	private UsuarioWebUsernameGetResponse wsAutenticar(String origin, String username, String password)
			throws Exception, InterruptedException, ExecutionException, PresentableUnloggedException {
		String url = "usuario-web";
		if (origin != null && origin.startsWith("int"))
			url = "usuario";

		UsuarioWebUsernameGetRequest q = new UsuarioWebUsernameGetRequest();
		q.username = username;

		String authorization = "Basic " + SwaggerUtils.base64Encode((username + ":" + password).getBytes());
		Future<SwaggerAsyncResponse<UsuarioWebUsernameGetResponse>> future = SwaggerCall.callAsync("autenticar usuário",
				authorization, "GET", Utils.getWsProcessualUrl() + "/" + url + "/" + username, q,
				UsuarioWebUsernameGetResponse.class);
		SwaggerAsyncResponse<UsuarioWebUsernameGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw new PresentableUnloggedException(sar.getException().getLocalizedMessage(), sar.getException());
		UsuarioWebUsernameGetResponse r = (UsuarioWebUsernameGetResponse) sar.getResp();
		return r;
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
		Long id;
		Long unidade;
	}

	public static class Usuario {
		String origem;
		String email;
		String nome;
		String usuario;
		String cpf;
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
		String users = (String) jwt.get("users");
		if (users != null && users.length() > 0) {
			u.usuarios = new HashMap<>();
			for (String s : users.split(";")) {
				String[] ss = s.split(",");
				UsuarioDetalhe ud = new UsuarioDetalhe();
				if (!"null".equals(ss[1]))
					ud.id = Long.valueOf(ss[1]);
				if (!"null".equals(ss[2]))
					ud.unidade = Long.valueOf(ss[2]);
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

	private static String jwt(String origin, String username, String cpf, String name, String email, String users) {
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
		claims.put("cpf", cpf);
		claims.put("name", name);
		claims.put("email", email);
		claims.put("users", users);

		final String jwt = signer.sign(claims);
		return jwt;
	}

	@Override
	public String getContext() {
		return "autenticar usuário";
	}

}

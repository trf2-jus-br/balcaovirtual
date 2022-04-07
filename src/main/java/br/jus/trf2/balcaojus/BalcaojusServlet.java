package br.jus.trf2.balcaojus;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.auth0.jwt.JWTVerifyException;
import com.crivano.swaggerservlet.SwaggerContext;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.crivano.swaggerservlet.dependency.SwaggerServletDependency;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222;
import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.util.AcessoInvalidoException;
import br.jus.trf2.balcaojus.util.AcessoProibidoException;
import br.jus.trf2.balcaojus.util.AcessoPublico;
import br.jus.trf2.balcaojus.util.AcessoPublicoEPrivado;

public class BalcaojusServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	public static final long TIMEOUT_MILLISECONDS = 15000;
	public static  String JWT_AUTH_COOKIE_NAME;
	public static  String JWT_AUTH_COOKIE_DOMAIN;
	// Se 8h é a duração, informar 60 * 60 * 8
	public static  int JWT_AUTH_COOKIE_TIME_TO_EXPIRE_IN_S;
	// renova automaticamente X segundos antes de expirar
	public static  int JWT_AUTH_COOKIE_TIME_TO_RENEW_IN_S;

	static BalcaojusServlet INSTANCE = null;
	public static ExecutorService executor = null;

	@Override
	public void initialize(ServletConfig config) throws ServletException {
		INSTANCE = this;
		setAPI(IBalcaojus.class);

		setActionPackage("br.jus.trf2.balcaojus");

		addPublicProperty("systems");
		addPublicProperty("cert.systems", null);
		addPublicProperty("env");
		addPublicProperty("wootric.token", null);
		addPublicProperty("base.url", "http://localhost:8080/balcaojus");
		addPublicProperty("recaptcha.site.key");
		addPrivateProperty("recaptcha.secret.key");
		addRestrictedProperty("public.username");
		addPrivateProperty("public.password");

		addPrivateProperty("jwt.secret");
		addPrivateProperty("api.secret");

		addPublicProperty("jwt.issuer", "bv");
		addPublicProperty("cookie.name", "bv-jwt");
		addPublicProperty("cookie.domain", null);
		addPublicProperty("cookie.expire.seconds", Long.toString(20 * 60L)); // Expira em 20min
		addPublicProperty("cookie.renew.seconds", Long.toString(15 * 60L)); // Renova 15min antes de expirar

		addRestrictedProperty("username.restriction", null);

		addRestrictedProperty("datasource.url", null);
		if (getProperty("datasource.url") != null) {
			addRestrictedProperty("datasource.username");
			addPrivateProperty("datasource.password");
			addRestrictedProperty("datasource.name", null);
		} else {
			addRestrictedProperty("datasource.username", null);
			addPrivateProperty("datasource.password", null);
			addRestrictedProperty("datasource.name", "balcaojusds");
		}

		// Redis
		//
		addRestrictedProperty("redis.database", "10");
		addPrivateProperty("redis.password", null);
		addRestrictedProperty("redis.slave.port", "0");
		addRestrictedProperty("redis.slave.host", null);
		addRestrictedProperty("redis.master.host", "localhost");
		addRestrictedProperty("redis.master.port", "6379");

		if (BalcaojusServlet.INSTANCE.getProperty("redis.password") != null)
			SwaggerUtils.setCache(new MemCacheRedis());

		// Notificações
		addPrivateProperty("notificar.password", null);
		addPublicProperty("notificar.titulo", null);
		addPublicProperty("notificar.url", null);

		// Firebase
		addPublicProperty("firebase.api.key", null);
		addPublicProperty("firebase.auth.domain", null);
		addPublicProperty("firebase.database.url", null);
		addPublicProperty("firebase.project.id", null);
		addPublicProperty("firebase.storage.bucket", null);
		addPublicProperty("firebase.messaging.sender.id", null);
		addPublicProperty("firebase.app.id", null);
		addPrivateProperty("firebase.server.key", null);

		// Threadpool
		addPublicProperty("threadpool.size", "10");
		executor = Executors
				.newFixedThreadPool(new Integer(BalcaojusServlet.INSTANCE.getProperty("threadpool.size")));

		for (String s : getProperty("systems").split(",")) {
			addPublicProperty(s.toLowerCase() + ".name");
			addRestrictedProperty(s.toLowerCase() + ".api.url");
			addPrivateProperty(s.toLowerCase() + ".api.password", null);
			if (s.toLowerCase().contains(".eproc")) {
				addRestrictedProperty(s.toLowerCase() + ".api.eproc.url");
				addRestrictedProperty(s.toLowerCase() + ".api.eproc.votos.url", null);
				addPrivateProperty(s.toLowerCase() + ".api.eproc.password", null);
			}
			addRestrictedProperty(s.toLowerCase() + ".mni.url");
			addRestrictedProperty(s.toLowerCase() + ".mni.endpoint");
			addPublicProperty(s.toLowerCase() + ".cota.tipo");
		}

		if (getProperty("cert.systems") != null) {
			for (String s : getProperty("cert.systems").split(",")) {
				addPublicProperty(s.toLowerCase() + ".cert.name");
				addPublicProperty(s.toLowerCase() + ".cert.fullname");
				addRestrictedProperty(s.toLowerCase() + ".cert.api.url");
				addPrivateProperty(s.toLowerCase() + ".cert.api.password");
			}
		}

		addRestrictedProperty("upload.dir.final");
		addRestrictedProperty("upload.dir.temp");

		addRestrictedProperty("smtp.remetente", null);
		addRestrictedProperty("smtp.host", null);
		addRestrictedProperty("smtp.host.alt", null);
		addRestrictedProperty("smtp.auth", "false");
		addRestrictedProperty("smtp.auth.usuario", null);
		addPrivateProperty("smtp.auth.senha", null);
		addRestrictedProperty("smtp.porta", "25");
		addRestrictedProperty("smtp.destinatario", null);
		addRestrictedProperty("smtp.assunto", "Balcãojus: Sugestão");
		addPublicProperty("votos.usuarios", null);

		class HttpGetDependency extends TestableDependency {
			String testsite;

			HttpGetDependency(String category, String service, String testsite, boolean partial, long msMin,
					long msMax) {
				super(category, service, partial, msMin, msMax);
				this.testsite = testsite;
			}

			@Override
			public String getUrl() {
				return testsite;
			}

			@Override
			public boolean test() throws Exception {
				final URL url = new URL(testsite);
				final URLConnection conn = url.openConnection();
				conn.connect();
				return true;
			}
		}

		class FileSystemWriteDependency extends TestableDependency {
			private static final String TESTING = "testing...";
			String path;

			FileSystemWriteDependency(String service, String path, boolean partial, long msMin, long msMax) {
				super("filesystem", service, partial, msMin, msMax);
				this.path = path;
			}

			@Override
			public String getUrl() {
				return path;
			}

			@Override
			public boolean test() throws Exception {
				Path file = Paths.get(path + "/test.temp");
				Files.write(file, TESTING.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
				String s = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
				return s != null;
			}
		}

		addDependency(
				new FileSystemWriteDependency("upload.dir.temp", getProperty("upload.dir.temp"), false, 0, 10000));

		addDependency(
				new FileSystemWriteDependency("upload.dir.final", getProperty("upload.dir.final"), false, 0, 10000));

		addDependency(new HttpGetDependency("rest", "www.google.com/recaptcha",
				"https://www.google.com/recaptcha/api/siteverify", false, 0, 10000));

		if (getProperty("notificar.titulo") != null) {
			addDependency(new HttpGetDependency("rest", "fcm.googleapis.com", "https://fcm.googleapis.com/fcm/send",
					false, 0, 10000));
		}

		for (final String system : Utils.getSystems()) {
			String systemSlug = system.replace(".", "-");
			addDependency(new SwaggerServletDependency(system, systemSlug + "-api", false, 0, 10000) {

				@Override
				public String getUrl() {
					return Utils.getApiUrl(system);
				}

			});

			if (system.contains(".eproc")) {
				addDependency(new SwaggerServletDependency(system, systemSlug + "-php-api", false, 0, 10000) {

					@Override
					public String getUrl() {
						return Utils.getApiEprocUrl(system);
					}

				});
			}

			addDependency(new TestableDependency(system, systemSlug + "-mni", false, 0, 10000) {

				@Override
				public String getUrl() {
					return Utils.getMniWsdlUrl(system);
				}

				@Override
				public boolean test() throws Exception {
					@SuppressWarnings("unused")
					ServicoIntercomunicacao222 client = SoapMNI.getClient(system);
					return true;
				}
			});
		}

		if (getProperty("cert.systems") != null) {
			for (String s : getProperty("cert.systems").split(",")) {
				String systemSlug = s.replace(".", "-");
				addPublicProperty(s.toLowerCase() + ".cert.name");
				addPublicProperty(s.toLowerCase() + ".cert.fullname");
				addRestrictedProperty(s.toLowerCase() + ".cert.api.url");
				addPrivateProperty(s.toLowerCase() + ".cert.api.password");
				addDependency(new HttpGetDependency(s, systemSlug + "-cert-api",
						getProperty(s.toLowerCase() + ".cert.api.url"), false, 0, 10000));
			}
		}

		addDependency(new TestableDependency("database", "balcaojusds", false, 0, 10000) {

			@Override
			public String getUrl() {
				return getProperty("datasource.name");
			}

			@Override
			public boolean test() throws Exception {
				try (Dao dao = new Dao()) {
					return dao.obtemData() != null;
				}
			}

			@Override
			public boolean isPartial() {
				return false;
			}
		});

		if (BalcaojusServlet.INSTANCE.getProperty("redis.password") != null)
			addDependency(new TestableDependency("cache", "redis", false, 0, 10000) {

				@Override
				public String getUrl() {
					return "redis://" + MemCacheRedis.getMasterHost() + ":" + MemCacheRedis.getMasterPort() + "/"
							+ MemCacheRedis.getDatabase() + " (" + "redis://" + MemCacheRedis.getSlaveHost() + ":"
							+ MemCacheRedis.getSlavePort() + "/" + MemCacheRedis.getDatabase() + ")";
				}

				@Override
				public boolean test() throws Exception {
					String uuid = UUID.randomUUID().toString();
					MemCacheRedis mc = new MemCacheRedis();
					mc.store("test", uuid.getBytes());
					String uuid2 = new String(mc.retrieve("test"));
					return uuid.equals(uuid2);
				}
			});

		JWT_AUTH_COOKIE_NAME = getProperty("cookie.name");
		JWT_AUTH_COOKIE_DOMAIN = getProperty("cookie.domain");
		JWT_AUTH_COOKIE_TIME_TO_EXPIRE_IN_S = Integer.valueOf(getProperty("cookie.expire.seconds"));
		JWT_AUTH_COOKIE_TIME_TO_RENEW_IN_S = Integer.valueOf(getProperty("cookie.renew.seconds"));

	}

	@Override
	public int errorStatus(Exception e) {
		return e.getMessage() == null || !e.getMessage().endsWith("(Alerta)") ? super.errorStatus(e) : 400;
	}

	@Override
	public String getService() {
		return "balcaojus";
	}

	@Override
	public String getUser() {
		try {
			Usuario u = AutenticarPost.assertUsuario();
			return u.usuario;
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> Future<T> submitToExecutor(Callable<T> task) {
		return executor.submit(task);
	}

	private final static ThreadLocal<Usuario> principal = new ThreadLocal<>();

	@Override
	public void invoke(SwaggerContext context) throws Exception {
		try {
			if (!context.getAction().getClass().isAnnotationPresent(AcessoPublico.class)) {
				try {
					principal.set(AutenticarPost.assertUsuario());
				} catch (Exception e) {
					if (!context.getAction().getClass().isAnnotationPresent(AcessoPublicoEPrivado.class))
						throw e;
				}
				if (principal.get() != null) {
					Map<String, Object> decodedToken = AutenticarPost.assertUsuarioAutorizado();
					final long now = System.currentTimeMillis() / 1000L;
					if ((Integer) decodedToken.get("exp") < now + JWT_AUTH_COOKIE_TIME_TO_RENEW_IN_S) {
						// Seria bom incluir o attributo HttpOnly
						String tokenNew = AutenticarPost.renew();
						Cookie cookie = AutenticarPost.buildCookie(tokenNew);
						context.getResponse().addCookie(cookie);
					}
				}
			}
			super.invoke(context);
		} catch (AcessoProibidoException e) {
			AutenticarPost.informarProibido(context.getResponse(), e);
			return;
		} catch (JWTVerifyException e) {
			if ("jwt expired".equals(e.getMessage()))
				AutenticarPost.informarNaoAutenticado(context.getResponse(), e);
			else
				throw new RuntimeException(e);
		} catch (AcessoInvalidoException e) {
			AutenticarPost.informarNaoAutenticado(context.getResponse(), e);
			return;
		} finally {
			principal.remove();
		}
	}

	public static Usuario getPrincipal() {
		return principal.get();
	}

}

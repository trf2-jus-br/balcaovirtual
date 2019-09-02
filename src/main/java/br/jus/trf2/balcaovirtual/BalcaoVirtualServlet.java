package br.jus.trf2.balcaovirtual;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.dependency.SwaggerServletDependency;
import com.crivano.swaggerservlet.dependency.TestableDependency;

import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222_Service;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class BalcaoVirtualServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void initialize(ServletConfig config) throws ServletException {
		setAPI(IBalcaoVirtual.class);

		setActionPackage("br.jus.trf2.balcaovirtual");

		addPublicProperty("systems");
		addPublicProperty("env");
		addPublicProperty("wootric.token", null);
		addPublicProperty("base.url", "http://localhost:8080/balcaovirtual");
		
		addRestrictedProperty("username.restriction", null);

		addRestrictedProperty("datasource.url", null);
		if (getProperty("datasource.url") != null) {
			addRestrictedProperty("datasource.username");
			addRestrictedProperty("datasource.password");
			addRestrictedProperty("datasource.name", null);
		} else {
			addRestrictedProperty("datasource.username", null);
			addRestrictedProperty("datasource.password", null);
			addRestrictedProperty("datasource.name", "balcaovirtualds");
		}

		for (String s : getProperty("systems").split(",")) {
			addRestrictedProperty(s.toLowerCase() + ".name");
			addRestrictedProperty(s.toLowerCase() + ".api.url");
			addRestrictedProperty(s.toLowerCase() + ".mni.url");
			addRestrictedProperty(s.toLowerCase() + ".mni.endpoint");
			addPublicProperty(s.toLowerCase() + ".cota.tipo");
		}

//		addPublicProperty("assijus.endpoint");
//		addPublicProperty("assijus.system.expedientes");
//		addPublicProperty("assijus.system.movimentos");

		addPrivateProperty("jwt.secret");
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
		addRestrictedProperty("smtp.assunto", "Balcão Virtual: Sugestão");

		class HttpGetDependency extends TestableDependency {
			String testsite;

			HttpGetDependency(String service, String testsite, boolean partial, long msMin, long msMax) {
				super("rest", service, partial, msMin, msMax);
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

		for (final String system : Utils.getSystems()) {
			addDependency(new SwaggerServletDependency(system.toLowerCase(), "api", false, 0, 10000) {

				@Override
				public String getUrl() {
					return Utils.getApiUrl(system);
				}

				@Override
				public String getResponsable() {
					return null;
				}

			});

			addDependency(new TestableDependency(system.toLowerCase(), "mni", false, 0, 10000) {

				@Override
				public String getUrl() {
					return Utils.getMniWsdlUrl(system);
				}

				@Override
				public boolean test() throws Exception {
					URL url = new URL(Utils.getMniWsdlUrl(system));
					ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
					@SuppressWarnings("unused")
					ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
					return true;
				}
			});
		}

		addDependency(new TestableDependency("database", "balcaovirtualds", false, 0, 10000) {
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

	}

	@Override
	public int errorCode(Exception e) {
		return e.getMessage() == null || !e.getMessage().endsWith("(Alerta)") ? super.errorCode(e) : 400;
	}

	@Override
	public String getService() {
		return "balcaovirtual";
	}

	@Override
	public String getUser() {
		try {
			Usuario u = SessionsCreatePost.assertUsuario();
			return u.usuario;
		} catch (Exception e) {
			return null;
		}
	}

}

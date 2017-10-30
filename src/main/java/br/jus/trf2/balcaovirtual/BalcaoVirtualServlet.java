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
import com.crivano.swaggerservlet.SwaggerUtils;
import com.crivano.swaggerservlet.dependency.TestableDependency;
import com.crivano.swaggerservlet.property.PrivateProperty;
import com.crivano.swaggerservlet.property.PublicProperty;
import com.crivano.swaggerservlet.property.RestrictedProperty;

import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222_Service;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class BalcaoVirtualServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		super.setAPI(IBalcaoVirtual.class);

		super.setActionPackage("br.jus.trf2.balcaovirtual");

		super.addProperty(new PublicProperty("balcaovirtual.env"));
		super.addProperty(new PublicProperty("balcaovirtual.wootric.token"));
		super.addProperty(new RestrictedProperty("balcaovirtual.ws.processual.url"));
		super.addProperty(new RestrictedProperty("balcaovirtual.ws.documental.url"));
		super.addProperty(new PublicProperty("balcaovirtual.orgaos"));

		for (String s : SwaggerUtils.getProperty("balcaovirtual.orgaos", "").split(",")) {
			super.addProperty(new RestrictedProperty("balcaovirtual.mni." + s.toLowerCase() + ".url"));
			super.addProperty(new PublicProperty("balcaovirtual." + s.toLowerCase() + ".cota.tipo"));
		}

		super.addProperty(new PublicProperty("balcaovirtual.assijus.endpoint"));
		super.addProperty(new PublicProperty("balcaovirtual.assijus.system.expedientes"));
		super.addProperty(new PublicProperty("balcaovirtual.assijus.system.movimentos"));

		super.addProperty(new PrivateProperty("balcaovirtual.jwt.secret"));
		super.addProperty(new RestrictedProperty("balcaovirtual.upload.dir.final"));
		super.addProperty(new RestrictedProperty("balcaovirtual.upload.dir.temp"));

		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.remetente"));
		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.host"));
		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.host.alt"));
		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.auth"));
		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.auth.usuario"));
		super.addProperty(new PrivateProperty("balcaovirtual.smtp.auth.senha"));
		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.porta"));
		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.destinatario"));
		super.addProperty(new RestrictedProperty("balcaovirtual.smtp.assunto"));

		super.setAuthorizationToProperties(SwaggerUtils.getProperty("balcaovirtual.properties.secret", null));

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

		addDependency(new FileSystemWriteDependency("upload.dir.temp",
				SwaggerUtils.getProperty("balcaovirtual.upload.dir.temp", "[undefined]"), false, 0, 10000));

		addDependency(new FileSystemWriteDependency("upload.dir.final",
				SwaggerUtils.getProperty("balcaovirtual.upload.dir.final", "[undefined]"), false, 0, 10000));

		addDependency(new HttpGetDependency("apolows", Utils.getWsProcessualUrl() + "/classe-cnj/81?orgao=TRF2", false,
				0, 10000));

		String[] systems = Utils.getOrgaos().split(",");
		for (final String system : systems) {
			addDependency(new TestableDependency("soap", "mni-" + system.toLowerCase(), false, 0, 10000) {

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
				return SwaggerUtils.getProperty("balcaovirtual.datasource.name", "balcaovirtualds");
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

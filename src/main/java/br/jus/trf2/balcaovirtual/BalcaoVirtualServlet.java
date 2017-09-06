package br.jus.trf2.balcaovirtual;

import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.crivano.swaggerservlet.dependency.TestableDependency;
import com.crivano.swaggerservlet.property.PublicProperty;

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

		super.addProperty(new PublicProperty("balcaovirtual.marcas"));
		super.addProperty(new PublicProperty("balcaovirtual.env"));

		class HttpGetDependency extends TestableDependency {
			String testsite;

			HttpGetDependency(String service, String testsite, boolean partial, long msMin, long msMax) {
				super("rest webservice", service, partial, msMin, msMax);
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

		addDependency(new HttpGetDependency("apolows", Utils.getWsProcessualUrl() + "/classe-cnj/81?orgao=TRF2", false,
				0, 10000));

		String[] systems = Utils.getOrgaos().split(",");
		for (final String system : systems) {
			addDependency(new TestableDependency("soap webservice", "mni-" + system.toLowerCase(), false, 0, 10000) {

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

		if (Utils.getMarcasAtivas()) {
			addDependency(new TestableDependency("database", "balcaovirtualds", false, 0, 10000) {
				@Override
				public String getUrl() {
					return SwaggerUtils.getProperty("balcaovirtual.datasource.name", "balcaovirtualds");
				}

				@Override
				public boolean test() throws Exception {
					Utils.getConnection().close();
					return true;
				}

				@Override
				public boolean isPartial() {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}

	}

	@Override
	public int errorCode(Exception e) {
		return e.getMessage() == null || !e.getMessage().endsWith("(Alerta)") ? 500 : 400;
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

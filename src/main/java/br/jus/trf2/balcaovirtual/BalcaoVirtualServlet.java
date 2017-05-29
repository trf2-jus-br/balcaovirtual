package br.jus.trf2.balcaovirtual;

import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.dependency.TestableDependency;

public class BalcaoVirtualServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		super.setAPI(IBalcaoVirtual.class);

		super.setActionPackage("br.jus.trf2.balcaovirtual");

		class HttpGetDependency extends TestableDependency {
			String testsite;

			HttpGetDependency(String service, String testsite, boolean partial, long msMin, long msMax) {
				super("webservice", service, partial, msMin, msMax);
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

		addDependency(new HttpGetDependency("apolows", Utils.getWsProcessualUrl() + "/classe-cnj/81?orgao=TRF2", false, 0, 10000));

		String[] systems = Utils.getOrgaos().split(",");
		for (final String system : systems) {
			addDependency(
					new HttpGetDependency("mni-" + system.toLowerCase(), Utils.getMniWsdlUrl(system), false, 0, 10000));
		}
	}

	@Override
	public String getService() {
		return "balcaovirtual";
	}

}

package br.jus.trf2.balcaovirtual;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;

public class BalcaoVirtualServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		super.setAPI(IBalcaoVirtual.class);

		super.setActionPackage("br.jus.trf2.balcaovirtual");
	}

	@Override
	public String getService() {
		return "balcaovirtual";
	}

}

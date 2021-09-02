package br.jus.trf2.balcaojus;

import com.crivano.swaggerservlet.SwaggerTestSupport;

import br.jus.trf2.balcaojus.BalcaojusServlet;

public class BalcaoVirtualServiceTest extends SwaggerTestSupport {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getAPI() {
		return BalcaojusServlet.class;
	}

	@Override
	protected String getPackage() {
		return "br.jus.trf2.balcaovirtual";
	}

	public void testNothing_Simple_Success() {
		assertEquals("1", "1");
	}

}

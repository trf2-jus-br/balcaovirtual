package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerTestSupport;

import br.jus.trf2.balcaovirtual.BalcaoVirtualServlet;

public class BalcaoVirtualServiceTest extends SwaggerTestSupport {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getAPI() {
		return BalcaoVirtualServlet.class;
	}

	@Override
	protected String getPackage() {
		// TODO Auto-generated method stub
		return "br.jus.trf2.balcaovirtual";
	}

	public void testNothing_Simple_Success() {
		assertEquals("1", "1");
	}

}

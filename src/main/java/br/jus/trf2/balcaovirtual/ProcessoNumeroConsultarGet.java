package br.jus.trf2.balcaovirtual;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroConsultarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetResponse;

public class ProcessoNumeroConsultarGet implements IProcessoNumeroConsultarGet {
	private static final Logger log = LoggerFactory.getLogger(ProcessoNumeroConsultarGet.class);

	@Override
	public void run(ProcessoNumeroConsultarGetRequest req, ProcessoNumeroConsultarGetResponse resp) throws Exception {
		String authorization = BalcaoVirtualServlet.getHttpServletRequest().getHeader("Authorization");
		if (authorization.startsWith("Bearer "))
			authorization = authorization.substring(7);
		Map<String, Object> jwt = SessionsCreatePost.verify(authorization);

		String json = SoapMNI.consultarProcesso(authorization, req.orgao, req.numero);
		resp.payload = json.getBytes(StandardCharsets.UTF_8);
		resp.contenttype = "application/json";
	}

	@Override
	public String getContext() {
		return "consultar processo";
	}

}

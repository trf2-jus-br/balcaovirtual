package br.jus.trf2.balcaovirtual;

import java.nio.charset.StandardCharsets;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroConsultarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetResponse;

public class ProcessoNumeroConsultarGet implements IProcessoNumeroConsultarGet {

	@Override
	public void run(ProcessoNumeroConsultarGetRequest req, ProcessoNumeroConsultarGetResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		String json = SoapMNI.consultarProcesso(authorization, req.orgao, req.numero);
		resp.payload = json.getBytes(StandardCharsets.UTF_8);
		resp.contenttype = "application/json";
	}

	@Override
	public String getContext() {
		return "consultar processo";
	}

}

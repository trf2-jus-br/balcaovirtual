package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigAdvogadoOabGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigAdvogadoOabGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigAdvogadoOabGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.AdvogadoDocumentoGetResponse;

public class ConfigAdvogadoOabGet implements IConfigAdvogadoOabGet {

	@Override
	public void run(ConfigAdvogadoOabGetRequest req, ConfigAdvogadoOabGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		Future<SwaggerAsyncResponse<AdvogadoDocumentoGetResponse>> future = SwaggerCall.callAsync("obter advogado",
				null, "GET", Utils.getApiUrl(req.sistema) + "/advogado/" + req.oab, null,
				AdvogadoDocumentoGetResponse.class);
		SwaggerAsyncResponse<AdvogadoDocumentoGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		AdvogadoDocumentoGetResponse r = (AdvogadoDocumentoGetResponse) sar.getResp();

		if (r.list == null || r.list.size() == 0)
			return;

		resp.sistema = req.sistema;
		resp.id = r.list.get(0).id;
		resp.nome = r.list.get(0).nome;
		resp.oab = r.list.get(0).documento;
	}

	@Override
	public String getContext() {
		return "obter dados de advogado";
	}

}

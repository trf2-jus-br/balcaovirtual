package br.jus.trf2.balcaojus;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.IBalcaojus.IConfigAdvogadoOabGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IAdvogadoDocumentoGet;

public class ConfigAdvogadoOabGet implements IConfigAdvogadoOabGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Future<SwaggerAsyncResponse<IAdvogadoDocumentoGet.Response>> future = SwaggerCall.callAsync("obter advogado",
				Utils.getApiPassword(req.sistema), "GET", Utils.getApiUrl(req.sistema) + "/advogado/" + req.oab, null,
				IAdvogadoDocumentoGet.Response.class);
		SwaggerAsyncResponse<IAdvogadoDocumentoGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IAdvogadoDocumentoGet.Response r = (IAdvogadoDocumentoGet.Response) sar.getResp();

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

package br.jus.trf2.balcaojus;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.IBalcaojus.IConfigPessoaFisicaCpfGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IPessoaFisicaDocumentoGet;

public class ConfigPessoaFisicaCpfGet implements IConfigPessoaFisicaCpfGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Future<SwaggerAsyncResponse<IPessoaFisicaDocumentoGet.Response>> future = SwaggerCall.callAsync(
				"obter pessoa física", Utils.getApiPassword(req.sistema), "GET",
				Utils.getApiUrl(req.sistema) + "/pessoa-fisica/" + req.cpf, null,
				IPessoaFisicaDocumentoGet.Response.class);
		SwaggerAsyncResponse<IPessoaFisicaDocumentoGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IPessoaFisicaDocumentoGet.Response r = (IPessoaFisicaDocumentoGet.Response) sar.getResp();

		if (r.list == null || r.list.size() == 0)
			return;

		resp.sistema = req.sistema;
		resp.id = r.list.get(0).id;
		resp.nome = r.list.get(0).nome;
		resp.tipodedocumento = r.list.get(0).tipodedocumento;
		resp.documento = r.list.get(0).documento;
	}

	@Override
	public String getContext() {
		return "obter dados de pessoa física";
	}

}

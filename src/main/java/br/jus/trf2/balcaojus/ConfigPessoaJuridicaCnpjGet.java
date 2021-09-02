package br.jus.trf2.balcaojus;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.IBalcaojus.IConfigPessoaJuridicaCnpjGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IPessoaJuridicaDocumentoGet;

public class ConfigPessoaJuridicaCnpjGet implements IConfigPessoaJuridicaCnpjGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Future<SwaggerAsyncResponse<IPessoaJuridicaDocumentoGet.Response>> future = SwaggerCall.callAsync(
				"obter pessoa jurídica", Utils.getApiPassword(req.sistema), "GET",
				Utils.getApiUrl(req.sistema) + "/pessoa-juridica/" + req.cnpj, null,
				IPessoaJuridicaDocumentoGet.Response.class);
		SwaggerAsyncResponse<IPessoaJuridicaDocumentoGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IPessoaJuridicaDocumentoGet.Response r = (IPessoaJuridicaDocumentoGet.Response) sar.getResp();

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
		return "obter dados de pessoa jurídica";
	}

}

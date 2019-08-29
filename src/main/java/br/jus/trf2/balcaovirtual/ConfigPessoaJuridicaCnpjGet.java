package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPessoaJuridicaCnpjGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPessoaJuridicaCnpjGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigPessoaJuridicaCnpjGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.PessoaJuridicaDocumentoGetResponse;

public class ConfigPessoaJuridicaCnpjGet implements IConfigPessoaJuridicaCnpjGet {

	@Override
	public void run(ConfigPessoaJuridicaCnpjGetRequest req, ConfigPessoaJuridicaCnpjGetResponse resp) throws Exception {
		SessionsCreatePost.assertAuthorization();

		Future<SwaggerAsyncResponse<PessoaJuridicaDocumentoGetResponse>> future = SwaggerCall.callAsync(
				"obter pessoa jurídica", null, "GET",
				Utils.getApiUrl(req.sistema) + "/pessoa-juridica/" + req.cnpj, null,
				PessoaJuridicaDocumentoGetResponse.class);
		SwaggerAsyncResponse<PessoaJuridicaDocumentoGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		PessoaJuridicaDocumentoGetResponse r = (PessoaJuridicaDocumentoGetResponse) sar.getResp();

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

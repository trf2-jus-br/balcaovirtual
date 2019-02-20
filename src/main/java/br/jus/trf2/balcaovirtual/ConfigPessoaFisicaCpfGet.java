package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPessoaFisicaCpfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPessoaFisicaCpfGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigPessoaFisicaCpfGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.PessoaFisicaDocumentoGetResponse;

public class ConfigPessoaFisicaCpfGet implements IConfigPessoaFisicaCpfGet {

	@Override
	public void run(ConfigPessoaFisicaCpfGetRequest req, ConfigPessoaFisicaCpfGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		Future<SwaggerAsyncResponse<PessoaFisicaDocumentoGetResponse>> future = SwaggerCall.callAsync(
				"obter pessoa física", null, "GET",
				Utils.getApiUrl(req.sistema) + "/pessoa-fisica/" + req.cpf + "?sistema=" + req.sistema, null,
				PessoaFisicaDocumentoGetResponse.class);
		SwaggerAsyncResponse<PessoaFisicaDocumentoGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		PessoaFisicaDocumentoGetResponse r = (PessoaFisicaDocumentoGetResponse) sar.getResp();

		if (r.list == null || r.list.size() == 0)
			return;

		resp.sistema = r.list.get(0).sistema;
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

package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadesGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Localidade;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.LocalidadeGetResponse;

public class ConfigLocalidadesGet implements IConfigLocalidadesGet {

	@Override
	public void run(ConfigLocalidadesGetRequest req, ConfigLocalidadesGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		Future<SwaggerAsyncResponse<LocalidadeGetResponse>> future = SwaggerCall.callAsync("obter localidades", null,
				"GET", Utils.getApiUrl(req.orgao) + "/localidade?orgao=" + req.orgao, null,
				LocalidadeGetResponse.class);
		SwaggerAsyncResponse<LocalidadeGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		LocalidadeGetResponse r = (LocalidadeGetResponse) sar.getResp();

		resp.list = new ArrayList<>();
		for (IdNome idNome : r.list) {
			Localidade o = new Localidade();
			o.id = idNome.id;
			o.nome = idNome.nome;
			resp.list.add(o);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de Localidades";
	}

}

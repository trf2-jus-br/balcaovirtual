package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEntidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEntidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Entidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigEntidadesGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.OrgaoPublicoListarGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Pessoa;

public class ConfigEntidadesGet implements IConfigEntidadesGet {

	@Override
	public void run(ConfigEntidadesGetRequest req, ConfigEntidadesGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		Future<SwaggerAsyncResponse<OrgaoPublicoListarGetResponse>> future = SwaggerCall.callAsync("obter localidades",
				null, "GET", Utils.getWsProcessualUrl() + "/orgao-publico/listar", null,
				OrgaoPublicoListarGetResponse.class);
		SwaggerAsyncResponse<OrgaoPublicoListarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		OrgaoPublicoListarGetResponse r = (OrgaoPublicoListarGetResponse) sar.getResp();

		resp.list = new ArrayList<>();
		for (Pessoa idNome : r.list) {
			Entidade o = new Entidade();
			o.orgao = idNome.orgao;
			o.id = idNome.id;
			o.nome = idNome.nome;
			resp.list.add(o);
		}
	}

	@Override
	public String getContext() {
		return "obter entidades";
	}

}

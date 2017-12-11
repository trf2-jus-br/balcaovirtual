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
				null, "GET", Utils.getWsProcessualUrl() + "/orgao-publico/listar?tipodedocumento=cnpj", null,
				OrgaoPublicoListarGetResponse.class);
		SwaggerAsyncResponse<OrgaoPublicoListarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		OrgaoPublicoListarGetResponse r = (OrgaoPublicoListarGetResponse) sar.getResp();

		resp.list = new ArrayList<>();
		for (Pessoa p : r.list) {
			Entidade o = new Entidade();
			o.orgao = p.orgao;
			o.id = p.id;
			o.nome = p.nome;
			o.documento = p.documento;
			o.tipodedocumento = p.tipodedocumento;
			resp.list.add(o);
		}
	}

	@Override
	public String getContext() {
		return "obter entidades";
	}

}

package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEntidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEntidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Entidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigEntidadesGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.OrgaoPublicoListarGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Pessoa;

public class ConfigEntidadesGet implements IConfigEntidadesGet {

	@Override
	// TODO: Trocar órgão por system
	public void run(ConfigEntidadesGetRequest req, ConfigEntidadesGetResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter localidades", null, "GET",
							Utils.getApiUrl(system) + "/orgao-publico/listar?tipodedocumento=cnpj", null,
							OrgaoPublicoListarGetResponse.class));
		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, SessionsCreatePost.TIMEOUT_MILLISECONDS);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			OrgaoPublicoListarGetResponse r = (OrgaoPublicoListarGetResponse) mcr.responses.get(system);
			if (r.list != null)
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
	}

	@Override
	public String getContext() {
		return "obter entidades";
	}

}

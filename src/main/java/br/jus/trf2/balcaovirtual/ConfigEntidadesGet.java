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
	public void run(ConfigEntidadesGetRequest req, ConfigEntidadesGetResponse resp) throws Exception {
		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter localidades", Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/orgao-publico/listar?tipodedocumento=cnpj", null,
							OrgaoPublicoListarGetResponse.class));
		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, AutenticarPost.TIMEOUT_MILLISECONDS);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			OrgaoPublicoListarGetResponse r = (OrgaoPublicoListarGetResponse) mcr.responses.get(system);
			if (r.list != null)
				for (Pessoa p : r.list) {
					Entidade o = new Entidade();
					o.sistema = system;
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

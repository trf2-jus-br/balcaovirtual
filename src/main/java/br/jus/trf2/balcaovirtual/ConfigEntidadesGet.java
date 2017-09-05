package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEntidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEntidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Entidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigEntidadesGet;

public class ConfigEntidadesGet implements IConfigEntidadesGet {

	@Override
	public void run(ConfigEntidadesGetRequest req, ConfigEntidadesGetResponse resp) throws Exception {
		resp.list = new ArrayList<>();
		Entidade entidade = new Entidade();
		entidade.id = "1";
		entidade.nome = "CJF";
		resp.list.add(entidade);
	}

	@Override
	public String getContext() {
		return "obter entidades";
	}

}

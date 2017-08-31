package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPeticaoInicialDadosBasicosGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPeticaoInicialDadosBasicosGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Entidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigPeticaoInicialDadosBasicosGet;

public class ConfigPeticaoInicialDadosBasicosGet implements IConfigPeticaoInicialDadosBasicosGet {
	private static final Logger log = LoggerFactory.getLogger(ConfigPeticaoInicialDadosBasicosGet.class);

	@Override
	public void run(ConfigPeticaoInicialDadosBasicosGetRequest req, ConfigPeticaoInicialDadosBasicosGetResponse resp)
			throws Exception {
		resp.listentidade = new ArrayList<>();
		Entidade entidade = new Entidade();
		entidade.id = "1";
		entidade.nome = "CJF";
		resp.listentidade.add(entidade);
	}

	@Override
	public String getContext() {
		return "obter dados básicos de petição inicial";
	}

}

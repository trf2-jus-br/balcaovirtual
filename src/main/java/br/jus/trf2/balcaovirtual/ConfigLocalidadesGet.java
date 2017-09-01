package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadesGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Localidade;

public class ConfigLocalidadesGet implements IConfigLocalidadesGet {
	private static final Logger log = LoggerFactory.getLogger(ConfigLocalidadesGet.class);

	@Override
	public void run(ConfigLocalidadesGetRequest req, ConfigLocalidadesGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		resp.list = new ArrayList<>();
		if (req.orgao.equals("jfrj")) {
			{
				Localidade o = new Localidade();
				o.id = "11";
				o.nome = "ANGRA DOS REIS";
				resp.list.add(o);
			}
			{
				Localidade o = new Localidade();
				o.id = "19";
				o.nome = "BARRA DO PIRAÍ";
				resp.list.add(o);
			}
		} else if (req.orgao.equals("trf2")) {
			{
				Localidade o = new Localidade();
				o.id = "1";
				o.nome = "RIO DE JANEIRO";
				resp.list.add(o);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter lista de Localidades";
	}

}

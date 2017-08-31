package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocaisGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocaisGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocaisGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Local;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ConfigLocaisGet implements IConfigLocaisGet {
	private static final Logger log = LoggerFactory.getLogger(ConfigLocaisGet.class);

	@Override
	public void run(ConfigLocaisGetRequest req, ConfigLocaisGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		resp.list = new ArrayList<>();
		if (req.orgao.equals("jfrj")) {
			{
				Local o = new Local();
				o.id = "11";
				o.nome = "ANGRA DOS REIS";
				resp.list.add(o);
			}
			{
				Local o = new Local();
				o.id = "19";
				o.nome = "BARRA DO PIRAÍ";
				resp.list.add(o);
			}
		} else if (req.orgao.equals("trf2")) {
			{
				Local o = new Local();
				o.id = "1";
				o.nome = "RIO DE JANEIRO";
				resp.list.add(o);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter lista de locais";
	}

}

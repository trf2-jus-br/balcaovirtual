package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigSistemasGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigSistemasGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigSistemasGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SistemaInfo;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ConfigSistemasGet implements IConfigSistemasGet {

	@Override
	public void run(ConfigSistemasGetRequest req, ConfigSistemasGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();

		resp.list = new ArrayList<>();

		// TODO: Trocar órgão por system
		for (String sistema : Utils.getSystems()) {
			String system = sistema.toLowerCase();
			if (!u.usuarios.containsKey(system))
				continue;
			SistemaInfo oi = new SistemaInfo();
			oi.id = system;
			oi.nome = sistema;
			resp.list.add(oi);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de órgãos";
	}

}

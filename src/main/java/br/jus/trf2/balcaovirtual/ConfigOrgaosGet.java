package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigOrgaosGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigOrgaosGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigOrgaosGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.OrgaoInfo;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ConfigOrgaosGet implements IConfigOrgaosGet {

	@Override
	public void run(ConfigOrgaosGetRequest req, ConfigOrgaosGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();

		resp.list = new ArrayList<>();

		// TODO: Trocar órgão por system
		for (String orgao : Utils.getSystems()) {
			String system = orgao.toLowerCase();
			if (!u.usuarios.containsKey(system))
				continue;
			OrgaoInfo oi = new OrgaoInfo();
			oi.id = system;
			oi.nome = orgao;
			resp.list.add(oi);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de órgãos";
	}

}

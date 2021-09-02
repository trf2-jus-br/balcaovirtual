package br.jus.trf2.balcaojus;

import java.util.ArrayList;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IConfigSistemasGet;
import br.jus.trf2.balcaojus.IBalcaojus.SistemaInfo;

public class ConfigSistemasGet implements IConfigSistemasGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		resp.list = new ArrayList<>();

		for (String system : Utils.getSystems()) {
			if (!u.usuarios.containsKey(system))
				continue;
			if (system.contains(".apolo"))
				continue;
			SistemaInfo oi = new SistemaInfo();
			oi.id = system;
			oi.nome = Utils.getName(system);
			resp.list.add(oi);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de órgãos";
	}

}

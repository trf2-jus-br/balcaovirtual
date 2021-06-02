package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigSistemasGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SistemaInfo;

public class ConfigSistemasGet implements IConfigSistemasGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

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

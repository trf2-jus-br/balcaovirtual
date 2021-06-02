package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPadraoIdGet;
import br.jus.trf2.balcaovirtual.model.Padrao;

public class PadraoIdGet implements IPadraoIdGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		try (Dao dao = new Dao()) {
			Padrao i = dao.find(Padrao.class, Long.parseLong(req.id));
			resp.padrao = PadraoGet.formatar(i);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de padrões";
	}

}

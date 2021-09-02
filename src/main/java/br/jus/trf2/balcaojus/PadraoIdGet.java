package br.jus.trf2.balcaojus;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IPadraoIdGet;
import br.jus.trf2.balcaojus.model.Padrao;

public class PadraoIdGet implements IPadraoIdGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

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

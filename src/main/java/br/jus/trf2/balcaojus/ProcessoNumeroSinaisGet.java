package br.jus.trf2.balcaojus;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroSinaisGet;
import br.jus.trf2.balcaojus.IBalcaojus.Processo;
import br.jus.trf2.balcaojus.model.Sinal;

public class ProcessoNumeroSinaisGet implements IProcessoNumeroSinaisGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		try (Dao dao = new Dao()) {
			Sinal s = dao.obtemSinais(u.isInterno(), u.usuario, req.numero);

			resp.processo = new Processo();
			resp.processo.numero = req.numero;

			if (s != null) {
				resp.processo.favorito = s.getSinaLgFavorito();
				resp.processo.recente = s.getSinaDfRecente();
			}
		}
	}

	@Override
	public String getContext() {
		return "obter sinais";
	}

}

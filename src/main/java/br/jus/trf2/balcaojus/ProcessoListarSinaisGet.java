package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.List;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoListarSinaisGet;
import br.jus.trf2.balcaojus.model.Sinal;

public class ProcessoListarSinaisGet implements IProcessoListarSinaisGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		resp.list = new ArrayList<>();

		try (Dao dao = new Dao()) {
			List<Sinal> l = dao.obtemSinais(u.isInterno(), u.usuario);

			if (l == null)
				return;

			for (Sinal sinal : l) {
				br.jus.trf2.balcaojus.IBalcaojus.Processo p = new br.jus.trf2.balcaojus.IBalcaojus.Processo();
				p.numero = sinal.getSinaCdProc();
				p.favorito = sinal.getSinaLgFavorito();
				p.recente = sinal.getSinaDfRecente();
				resp.list.add(p);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter sinais de processos";
	}

}

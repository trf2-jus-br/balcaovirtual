package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.List;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoListarSinaisGet;
import br.jus.trf2.balcaovirtual.model.Sinal;

public class ProcessoListarSinaisGet implements IProcessoListarSinaisGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		resp.list = new ArrayList<>();

		try (Dao dao = new Dao()) {
			List<Sinal> l = dao.obtemSinais(u.isInterno(), u.usuario);

			if (l == null)
				return;

			for (Sinal sinal : l) {
				br.jus.trf2.balcaovirtual.IBalcaoVirtual.Processo p = new br.jus.trf2.balcaovirtual.IBalcaoVirtual.Processo();
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

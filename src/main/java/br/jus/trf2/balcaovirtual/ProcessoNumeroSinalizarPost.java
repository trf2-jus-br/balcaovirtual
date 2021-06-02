package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroSinalizarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Processo;
import br.jus.trf2.balcaovirtual.model.Sinal;

public class ProcessoNumeroSinalizarPost implements IProcessoNumeroSinalizarPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		try (Dao dao = new Dao()) {
			Sinal s = dao.obtemSinais(u.isInterno(), u.usuario, req.numero);
			if (s == null) {
				s = new Sinal();
				s.setSinaCdProc(req.numero);
				s.setSinaCdUsu(u.usuario);
				s.setSinaLgInterno(u.isInterno());
			}
			if (req.favorito != null)
				s.setSinaLgFavorito(req.favorito);
			if (req.recente != null)
				if (req.recente)
					s.setSinaDfRecente(dao.obtemData());
				else
					s.setSinaDfRecente(null);
			dao.persist(s);
			if (!s.getSinaLgFavorito() && s.getSinaDfRecente() == null)
				dao.remove(s);

			resp.processo = new Processo();
			resp.processo.numero = req.numero;

			if (s != null) {
				resp.processo.favorito = s.getSinaLgFavorito();
				resp.processo.recente = s.getSinaDfRecente();
			}
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "gravar sinal";
	}

}

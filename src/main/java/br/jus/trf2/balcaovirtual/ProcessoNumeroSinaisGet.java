package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroSinaisGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Processo;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroSinaisGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroSinaisGetResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.model.Sinal;

public class ProcessoNumeroSinaisGet implements IProcessoNumeroSinaisGet {

	@Override
	public void run(ProcessoNumeroSinaisGetRequest req, ProcessoNumeroSinaisGetResponse resp) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

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

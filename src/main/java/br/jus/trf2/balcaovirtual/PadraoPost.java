package br.jus.trf2.balcaovirtual;

import java.util.Date;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPadraoPost;
import br.jus.trf2.balcaovirtual.model.Padrao;

public class PadraoPost implements IPadraoPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		try (Dao dao = new Dao()) {
			Padrao p = (req.id != null) ? dao.find(Padrao.class, Long.valueOf(req.id)) : new Padrao();
			p.setPadrTxConteudo(req.html);
			p.setPadrCdUsu(u.usuario);
			if (req.id == null)
				p.setPadrDfInclusao(new Date());
			if (p.getPadrDfModificacao() == null)
				p.setPadrDfModificacao(p.getPadrDfInclusao());
			dao.persist(p);
			resp.padrao = PadraoGet.formatar(p);
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "gravar padrão";
	}

}

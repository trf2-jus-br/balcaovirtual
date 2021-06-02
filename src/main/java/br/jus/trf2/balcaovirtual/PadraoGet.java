package br.jus.trf2.balcaovirtual;

import java.util.List;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPadraoGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PadraoItem;
import br.jus.trf2.balcaovirtual.model.Padrao;

public class PadraoGet implements IPadraoGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		try (Dao dao = new Dao()) {
			List<Padrao> l = dao.obtemPadroes(u.usuario);
			for (Padrao i : l) {
				PadraoItem d = formatar(i);
//				d.numeroDoDocumento;
//				d.autor;
//				d.reu;
//				d.descricao;
//				d.status;
//				d.descricaoDoStatus;
//				d.tipoDoDocumento;
//				d.identificadorDoUsuarioQueIncluiu;
//				d.nomeDoUsuarioQueIncluiu;
//				d.siglaDaUnidade;
				resp.list.add(d);
			}
		}
	}

	public static PadraoItem formatar(Padrao i) {
		PadraoItem d = new PadraoItem();
		d.dataDeInclusao = i.getPadrDfInclusao();
		d.sistema = i.getSistema() != null ? i.getSistema().getSistSg() : null;
		d.conteudo = i.getPadrTxConteudo();
		d.id = i.getPadrId().toString();
		d.numeroDoProcesso = i.getPadrCdProc();
		return d;
	}

	@Override
	public String getContext() {
		return "obter lista de padrões";
	}

}

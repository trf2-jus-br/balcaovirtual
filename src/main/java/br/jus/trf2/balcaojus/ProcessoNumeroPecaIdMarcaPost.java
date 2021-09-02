package br.jus.trf2.balcaojus;

import java.util.List;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroPecaIdMarcaPost;
import br.jus.trf2.balcaojus.model.Estilo;
import br.jus.trf2.balcaojus.model.Marca;
import br.jus.trf2.balcaojus.model.Processo;
import br.jus.trf2.balcaojus.model.TipoMarcaItem;

public class ProcessoNumeroPecaIdMarcaPost implements IProcessoNumeroPecaIdMarcaPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());

		if (ud == null)
			throw new PresentableUnloggedException("Usuário '" + u.usuario
					+ "' não pode fazer marcações porque não foi autenticado no órgão '" + req.sistema + "'.");

		try (Dao dao = new Dao()) {
			Processo p = dao.obtemProcesso(req.numero, req.sistema, true);
			Marca m = null;
			if (req.idmarca != null)
				m = dao.find(Marca.class, Long.valueOf(req.idmarca));
			else
				m = new Marca();

			// verifica se o estilo está compatível com o usuário
			// (interno/externo)
			Estilo estilo = dao.find(Estilo.class, Long.valueOf(req.idestilo));
			if (estilo == null || (estilo.isEstiLgInterno() != u.isInterno()))
				throw new PresentableUnloggedException("Estilo inválido.");

			// verifica se o texto representa um tipo_marca_item
			List<TipoMarcaItem> l = dao.obtemTipoMarcaItens(Long.valueOf(req.idclasse));
			TipoMarcaItem tmi = null;
			String texto = req.texto;
			for (TipoMarcaItem i : l) {
				if (i.getTimiNm() != null && texto != null && texto.startsWith(i.getTimiNm())) {
					tmi = i;
					texto = texto.substring(tmi.getTimiNm().length());
					if (texto.startsWith(" - "))
						texto = texto.substring(3);
					texto = texto.trim();
					if (texto.length() == 0)
						texto = null;
					break;
				}
			}

			m.setMarcIdPeca(req.id);
			m.setProcesso(p);
			m.setTipoMarcaItem(tmi);
			m.setEstilo(estilo);
			m.setMarcTxConteudo(texto);
			m.setMarcNrPagInicial(req.paginicial != null ? Integer.valueOf(req.paginicial) : null);
			m.setMarcNrPagFinal(req.pagfinal != null ? Integer.valueOf(req.pagfinal) : null);
			m.setMarcLgInterno(u.isInterno());
			m.setMarcCdUsu(u.usuario);
			m.setMarcNmUsu(u.nome);
			m.setMarcIeUsu(ud.id);
			m.setMarcIeUnidade(ud.codunidade);
			m.setMarcDfAlteracao(dao.obtemData());
			dao.persist(m);

			// Produce response
			resp.marca = new br.jus.trf2.balcaojus.IBalcaojus.Marca();
			resp.marca.idmarca = Long.toString(m.getMarcId());
			resp.marca.idpeca = req.id;
			resp.marca.texto = tmi != null ? tmi.getTimiNm() + (texto != null ? " - " + texto : "") : texto;
			resp.marca.idestilo = req.idestilo;
			resp.marca.paginicial = req.paginicial;
			resp.marca.pagfinal = req.pagfinal;
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "gravar marca";
	}

}

package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroMarcasGet;
import br.jus.trf2.balcaojus.IBalcaojus.Marca;
import br.jus.trf2.balcaojus.model.Processo;

public class ProcessoNumeroMarcasGet implements IProcessoNumeroMarcasGet {
	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.usuarios == null)
			throw new PresentableException("Usuário não possui identificador e unidade");
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());

		resp.list = new ArrayList<>();

		if (ud == null)
			throw new PresentableUnloggedException("disabled");

		try (Dao dao = new Dao()) {
			Processo p = dao.obtemProcesso(req.numero, req.sistema, true);
			List<Object[]> l = dao.obtemMarcas(p, u.isInterno(), ud.id, ud.codunidade);

			if (l == null)
				return;

			for (Object[] i : l) {
				br.jus.trf2.balcaojus.model.Marca m = (br.jus.trf2.balcaojus.model.Marca) i[0];
				String t = (String) i[1];

				Marca r = new Marca();
				r.dataalteracao = m.getMarcDfAlteracao();
				r.idestilo = Long.toString(m.getEstilo().getEstiId());
				r.idmarca = Long.toString(m.getMarcId());
				r.idpeca = m.getMarcIdPeca();
				r.nomeusuario = m.getMarcNmUsu();
				r.paginicial = m.getMarcNrPagInicial() != null ? m.getMarcNrPagInicial().toString() : null;
				r.pagfinal = m.getMarcNrPagFinal() != null ? m.getMarcNrPagFinal().toString() : null;
				r.texto = m.getMarcTxConteudo();
				r.texto = t != null ? t + (m.getMarcTxConteudo() != null ? " - " + m.getMarcTxConteudo() : "")
						: m.getMarcTxConteudo();
				resp.list.add(r);
			}
		}
	}

	@Override
	public String getContext() {
		return "consultar marcas";
	}

}

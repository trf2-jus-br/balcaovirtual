package br.jus.trf2.balcaojus;

import java.util.List;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroNotaPost;
import br.jus.trf2.balcaojus.model.Nota;
import br.jus.trf2.balcaojus.model.Processo;

public class ProcessoNumeroNotaPost implements IProcessoNumeroNotaPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());

		if (ud == null)
			throw new PresentableUnloggedException("Usuário '" + u.usuario
					+ "' não pode fazer anotações porque não foi autenticado no órgão '" + req.sistema + "'.");

		if (!req.pessoal && ud.codunidade == null)
			throw new PresentableUnloggedException(
					"Usuário '" + u.usuario + "' só pode fazer anotações pessoais no órgão '" + req.sistema + "'.");

		try (Dao dao = new Dao()) {
			Processo p = dao.obtemProcesso(req.numero, req.sistema, true);
			List<Nota> l = dao.obtemNotas(p, ud.id, ud.codunidade);
			for (Nota n : l) {
				if (n.getNotaLgPessoal() == req.pessoal)
					throw new PresentableUnloggedException(
							"Esta nota já foi criada por outro usuário, suas anotações não serão gravadas. Por favor, recarregue esta página e aplique novamente suas alterações.");
			}

			Nota nota = new Nota();
			nota.setProcesso(p);
			nota.setNotaLgPessoal(req.pessoal);
			nota.setNotaTxConteudo(req.texto);

			nota.setNotaLgInterno(u.isInterno());
			nota.setNotaCdUsu(u.usuario);
			nota.setNotaNmUsu(u.nome);
			nota.setNotaIeUnidade(ud.codunidade);
			nota.setNotaIeUsu(ud.id);
			nota.setNotaDfAlteracao(dao.obtemData());
			dao.persist(nota);

			resp.nota = new br.jus.trf2.balcaojus.IBalcaojus.Nota();
			resp.nota.idnota = Long.toString(nota.getNotaId());
			resp.nota.pessoal = nota.getNotaLgPessoal();
			resp.nota.texto = nota.getNotaTxConteudo();
			resp.nota.dataalteracao = nota.getNotaDfAlteracao();
			resp.nota.nomeusuario = nota.getNotaNmUsu();
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "criar nota";
	}

}

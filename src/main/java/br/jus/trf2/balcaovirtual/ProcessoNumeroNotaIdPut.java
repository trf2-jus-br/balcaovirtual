package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroNotaIdPut;
import br.jus.trf2.balcaovirtual.model.Nota;
import br.jus.trf2.balcaovirtual.model.Processo;

public class ProcessoNumeroNotaIdPut implements IProcessoNumeroNotaIdPut {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());

		if (ud == null)
			throw new PresentableUnloggedException("Usuário '" + u.usuario
					+ "' não pode fazer anotações porque não foi autenticado no órgão '" + req.sistema + "'.");

		try (Dao dao = new Dao()) {
			Processo p = dao.obtemProcesso(req.numero, req.sistema, true);
			dao.beginTransaction();
			Nota nota = dao.find(Nota.class, Long.valueOf(req.id));
			if (nota == null)
				throw new PresentableUnloggedException(
						"Esta nota foi removida por outro usuário, suas alterações não serão gravadas. Por favor, recarregue esta página e aplique novamente suas alterações.");
			if (nota.getNotaDfAlteracao().getTime() != req.dataalteracao.getTime())
				throw new PresentableUnloggedException(
						"Esta nota foi alterada por outro usuário, suas alterações não serão gravadas. Por favor, recarregue esta página e aplique novamente suas alterações.");
			if (p != nota.getProcesso())
				throw new Exception("identificadores de processo inválidos");
			nota.setNotaId(Long.valueOf(req.id));
			nota.setNotaTxConteudo(req.texto);

			nota.setNotaLgInterno(u.isInterno());
			nota.setNotaCdUsu(u.usuario);
			nota.setNotaNmUsu(u.nome);
			nota.setNotaIeUnidade(ud.codunidade);
			nota.setNotaIeUsu(ud.id);
			nota.setNotaDfAlteracao(dao.obtemData());
			dao.persist(nota);

			resp.nota = new br.jus.trf2.balcaovirtual.IBalcaoVirtual.Nota();
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
		return "alterar nota";
	}

}

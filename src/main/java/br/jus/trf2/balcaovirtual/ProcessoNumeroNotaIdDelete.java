package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroNotaIdDelete;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaIdDeleteRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaIdDeleteResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;
import br.jus.trf2.balcaovirtual.model.Nota;
import br.jus.trf2.balcaovirtual.model.Processo;

public class ProcessoNumeroNotaIdDelete implements IProcessoNumeroNotaIdDelete {

	@Override
	public void run(ProcessoNumeroNotaIdDeleteRequest req, ProcessoNumeroNotaIdDeleteResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());

		if (ud == null)
			throw new PresentableUnloggedException("Usuário '" + u.usuario
					+ "' não pode fazer anotações porque não foi autenticado no órgão '" + req.orgao + "'.");

		try (Dao dao = new Dao()) {
			Nota nota = dao.find(Nota.class, Long.valueOf(req.id));
			Processo p = dao.obtemProcesso(req.numero, req.orgao, false);
			if (p == null)
				throw new Exception("processo não encontrado");
			if (p != nota.getProcesso())
				throw new Exception("identificadores de processo inválidos");
			dao.beginTransaction();
			dao.remove(nota);
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "excluir nota";
	}

}

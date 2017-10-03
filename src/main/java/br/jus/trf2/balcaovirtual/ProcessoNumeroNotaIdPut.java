package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroNotaIdPut;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaIdPutRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaIdPutResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;
import br.jus.trf2.balcaovirtual.model.Nota;
import br.jus.trf2.balcaovirtual.model.Processo;

public class ProcessoNumeroNotaIdPut implements IProcessoNumeroNotaIdPut {

	@Override
	public void run(ProcessoNumeroNotaIdPutRequest req, ProcessoNumeroNotaIdPutResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());

		if (ud == null)
			throw new PresentableUnloggedException("Usuário '" + u.usuario
					+ "' não pode fazer anotações porque não foi autenticado no órgão '" + req.orgao + "'.");

		try (Dao dao = new Dao()) {
			Nota nota = dao.find(Nota.class, Long.valueOf(req.id));
			Processo p = dao.obtemProcesso(req.numero, req.orgao);
			if (p != nota.getProcesso())
				throw new Exception("identificadores de processo inválidos");
			nota.setNotaId(Long.valueOf(req.id));
			nota.setNotaTxConteudo(req.texto);

			nota.setNotaLgInterno(u.isInterno());
			nota.setNotaCdUsu(u.usuario);
			nota.setNotaNmUsu(u.nome);
			nota.setNotaIeUnidade(ud.unidade);
			nota.setNotaIeUsu(ud.id);
			dao.persist(nota);

			resp.nota = new br.jus.trf2.balcaovirtual.IBalcaoVirtual.Nota();
			resp.nota.idnota = Long.toString(nota.getNotaId());
			resp.nota.pessoal = nota.getNotaLgPessoal();
			resp.nota.texto = nota.getNotaTxConteudo();
			resp.nota.dataalteracao = nota.getNotaDfAlteracao();
			resp.nota.nomeusuario = nota.getNotaNmUsu();
		}
	}

	@Override
	public String getContext() {
		return "alterar nota";
	}

}

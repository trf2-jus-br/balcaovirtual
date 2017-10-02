package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroNotaPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroNotaPost implements IProcessoNumeroNotaPost {

	@Override
	public void run(ProcessoNumeroNotaPostRequest req, ProcessoNumeroNotaPostResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());

		if (ud == null)
			throw new PresentableUnloggedException("Usuário '" + u.usuario
					+ "' não pode fazer anotações porque não foi autenticado no órgão '" + req.orgao + "'.");
	}

	@Override
	public String getContext() {
		return "criar nota";
	}

}

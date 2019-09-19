package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ProcessoNumeroPdfGet implements IProcessoNumeroPdfGet {

	@Override
	public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception {
		String usuario = null;
		String senha = null;
		String origem;

		if (ProcessoNumeroValidarGet.assertValidToken(req.token, req.numero)) {
			origem = "pub";
		} else {
			Usuario u;
			u = SessionsCreatePost.assertUsuario();
			usuario = u.usuario;
			senha = u.senha;
			origem = u.origem;
		}
		resp.jwt = DownloadJwtFilenameGet.jwt(origem, usuario, senha, null, req.sistema, req.numero, null, null, null,
				null, null, null);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

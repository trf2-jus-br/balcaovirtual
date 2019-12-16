package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPecaIdPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPecaIdPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPecaIdPdfGetResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;

public class ProcessoNumeroPecaIdPdfGet implements IProcessoNumeroPecaIdPdfGet {

	@Override
	public void run(ProcessoNumeroPecaIdPdfGetRequest req, ProcessoNumeroPecaIdPdfGetResponse resp) throws Exception {
		String usuario = null;
		String senha = null;
		String origem;

		if (ProcessoNumeroValidarGet.isValidToken(req.token, req.numero)) {
			origem = "pub";
		} else {
			Usuario u;
			u = AutenticarPost.assertUsuario();
			usuario = u.usuario;
			senha = u.senha;
			if (u.usuarios.get(req.sistema) != null)
				origem = u.usuarios.get(req.sistema).origem;
			else
				origem = "pub";
		}
		resp.jwt = DownloadJwtFilenameGet.jwt(origem, usuario, senha, null, req.sistema, req.numero, req.id, null, null,
				null, null, null, null);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

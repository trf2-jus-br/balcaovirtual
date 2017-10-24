package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ProcessoNumeroPdfGet implements IProcessoNumeroPdfGet {

	@Override
	public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		resp.jwt = DownloadJwtFilenameGet.jwt(u.origem, u.usuario, null, req.orgao, req.numero, null, null, null);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

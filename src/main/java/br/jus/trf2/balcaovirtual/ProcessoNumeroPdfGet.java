package br.jus.trf2.balcaovirtual;

import java.util.UUID;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;

public class ProcessoNumeroPdfGet implements IProcessoNumeroPdfGet {

	@Override
	public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception {
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
			origem = u.origem;
		}
		resp.uuid = UUID.randomUUID().toString();
		Status.update(resp.uuid, "Aguardando na fila de tarefas", 0, 100, 0L);
		BalcaoVirtualServlet
				.submitToExecutor(new ProcessoCompleto(resp.uuid, usuario, senha, req.sistema, req.numero));
		resp.jwt = DownloadJwtFilenameGet.jwt(origem, usuario, senha, null, req.sistema, req.numero, null, null, null,
				null, null, null, resp.uuid);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

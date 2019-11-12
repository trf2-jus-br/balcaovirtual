package br.jus.trf2.balcaovirtual;

import java.util.UUID;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.AutenticarPost.UsuarioDetalhe;

public class ProcessoNumeroPdfGet implements IProcessoNumeroPdfGet {

	@Override
	public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception {
		String usuario = null;
		String senha = null;
		String origem = null;

		Usuario u = null;
		try {
			u = AutenticarPost.assertUsuario();
			UsuarioDetalhe detalhe = u.usuarios.get(req.sistema);
			if (detalhe != null) {
				usuario = u.usuario;
				senha = u.senha;
				origem = detalhe.origem;
			}
		} catch (Exception ex) {
		}

		if (usuario == null && (ProcessoNumeroValidarGet.isValidToken(req.token, req.numero) || u != null)) {
			usuario = SwaggerServlet.getProperty("public.username");
			senha = SwaggerServlet.getProperty("public.password");
			origem = "pub";
		}

		if (usuario == null)
			throw new PresentableUnloggedException("Usuário não possui login válido no sistema "
					+ Utils.getName(req.sistema) + " e também não passou pelo captcha");

		resp.uuid = UUID.randomUUID().toString();
		Status.update(resp.uuid, "Aguardando na fila de tarefas", 0, 100, 0L);
		BalcaoVirtualServlet.submitToExecutor(new ProcessoCompleto(resp.uuid, usuario, senha, req.sistema, req.numero));
		resp.jwt = DownloadJwtFilenameGet.jwt(origem, usuario, senha, null, req.sistema, req.numero, null, null, null,
				null, null, null, resp.uuid);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

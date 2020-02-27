package br.jus.trf2.balcaovirtual;

import java.util.UUID;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetResponse;
import br.jus.trf2.balcaovirtual.util.AcessoPublicoEPrivado;

@AcessoPublicoEPrivado
public class ProcessoNumeroPdfGet implements IProcessoNumeroPdfGet {

	@Override
	public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception {
		String usuario = null;
		String senha = null;
		String origem = null;

		Usuario u = null;
		try {
			u = BalcaoVirtualServlet.getPrincipal();
			UsuarioDetalhe detalhe = u.usuarios.get(req.sistema);
			if (detalhe != null) {
				usuario = u.usuario;
				senha = u.getSenha();
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

		resp.jwt = DownloadJwtFilenameGet.jwt(origem, usuario, null, req.sistema, req.numero, null, null, null, null,
				null, null, resp.uuid);
		usuario = Utils.preprocessarId(usuario, senha, req.sistema, origem);
		senha = Utils.preprocessarSenha(usuario, senha, req.sistema, origem);
		BalcaoVirtualServlet.submitToExecutor(new ProcessoCompleto(resp.uuid, usuario, senha, req.sistema, req.numero));
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

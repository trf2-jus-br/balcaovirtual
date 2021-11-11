package br.jus.trf2.balcaojus;

import java.util.UUID;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroPdfGet;
import br.jus.trf2.balcaojus.util.AcessoPublicoEPrivado;

@AcessoPublicoEPrivado
public class ProcessoNumeroPdfGet implements IProcessoNumeroPdfGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		String usuario = null;
		String senha = null;
		String origem = null;

		Usuario u = null;
		try {
			u = BalcaojusServlet.getPrincipal();
			UsuarioDetalhe detalhe = u.usuarios.get(req.sistema);
			if (detalhe != null) {
				usuario = u.usuario;
				senha = u.getSenha();
				origem = detalhe.origem;
			}
		} catch (Exception ex) {
		}

		if (usuario == null && (ProcessoValidarGet.isValidToken(req.token, req.numero) || u != null)) {
			usuario = BalcaojusServlet.INSTANCE.getProperty("public.username");
			senha = BalcaojusServlet.INSTANCE.getProperty("public.password");
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
		BalcaojusServlet.submitToExecutor(new ProcessoCompleto(resp.uuid, usuario, senha, req.sistema, req.numero));
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

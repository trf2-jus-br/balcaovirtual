package br.jus.trf2.balcaojus;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroPecaIdPdfGet;
import br.jus.trf2.balcaojus.util.AcessoPublicoEPrivado;

@AcessoPublicoEPrivado
public class ProcessoNumeroPecaIdPdfGet implements IProcessoNumeroPecaIdPdfGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		String usuario = null;
		String origem;

		if (ProcessoValidarGet.isValidToken(req.token, req.numero)) {
			origem = "pub";
		} else {
			Usuario u = BalcaojusServlet.getPrincipal();
			if (u == null)
				throw new SwaggerAuthorizationException("Usuário não autenticado e token inexistente ou inválido");
			usuario = u.usuario;
			if (u.usuarios.get(req.sistema) != null)
				origem = u.usuarios.get(req.sistema).origem;
			else
				origem = "pub";
		}
		resp.jwt = DownloadJwtFilenameGet.jwt(origem, usuario, null, req.sistema, req.numero, req.id, null, null, null,
				null, null, null);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

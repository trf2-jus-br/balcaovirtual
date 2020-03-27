package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPecaIdPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPecaIdPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPecaIdPdfGetResponse;
import br.jus.trf2.balcaovirtual.util.AcessoPublicoEPrivado;

@AcessoPublicoEPrivado
public class ProcessoNumeroPecaIdPdfGet implements IProcessoNumeroPecaIdPdfGet {

	@Override
	public void run(ProcessoNumeroPecaIdPdfGetRequest req, ProcessoNumeroPecaIdPdfGetResponse resp) throws Exception {
		String usuario = null;
		String origem;

		if (ProcessoNumeroValidarGet.isValidToken(req.token, req.numero)) {
			origem = "pub";
		} else {
			Usuario u = BalcaoVirtualServlet.getPrincipal();
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

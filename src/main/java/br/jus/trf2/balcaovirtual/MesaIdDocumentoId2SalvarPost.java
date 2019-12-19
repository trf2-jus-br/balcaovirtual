package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdDocumentoId2SalvarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdDocumentoId2SalvarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdDocumentoId2SalvarPostResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameMesaIdDocumentoId2SalvarPostRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameMesaIdDocumentoId2SalvarPostResponse;

public class MesaIdDocumentoId2SalvarPost implements IMesaIdDocumentoId2SalvarPost {

	@Override
	public void run(MesaIdDocumentoId2SalvarPostRequest req, MesaIdDocumentoId2SalvarPostResponse resp)
			throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		UsuarioUsernameMesaIdDocumentoId2SalvarPostRequest q = new UsuarioUsernameMesaIdDocumentoId2SalvarPostRequest();
		q.html = req.html.replace("&nbsp;", "&#160;");
		q.html = Html2Pdf.cleanHtmlJSoup(q.html, true);

		Future<SwaggerAsyncResponse<UsuarioUsernameMesaIdDocumentoId2SalvarPostResponse>> future = SwaggerCall
				.callAsync(
						getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocUrl(req.sistema)
								+ "/usuario/" + u.usuario + "/mesa/null/documento/" + req.id2 + "/salvar",
						q, UsuarioUsernameMesaIdDocumentoId2SalvarPostResponse.class);
		SwaggerAsyncResponse<UsuarioUsernameMesaIdDocumentoId2SalvarPostResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioUsernameMesaIdDocumentoId2SalvarPostResponse r = (UsuarioUsernameMesaIdDocumentoId2SalvarPostResponse) sar
				.getResp();

		resp.status = r.status;
	}

	@Override
	public String getContext() {
		return "salvar minuta";
	}

}

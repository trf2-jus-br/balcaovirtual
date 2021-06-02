package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdDocumentoId2SalvarPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameMesaIdDocumentoId2SalvarPost;

public class MesaIdDocumentoId2SalvarPost implements IMesaIdDocumentoId2SalvarPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Request q = new IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Request();
		q.html = req.html.replace("&nbsp;", "&#160;");
		q.html = Html2Pdf.cleanHtmlJSoup(q.html, true);

		Future<SwaggerAsyncResponse<IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Response>> future = SwaggerCall
				.callAsync(
						getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocUrl(req.sistema)
								+ "/usuario/" + u.usuario + "/mesa/null/documento/" + req.id2 + "/salvar",
						q, IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Response r = (IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Response) sar
				.getResp();

		resp.status = r.status;
	}

	@Override
	public String getContext() {
		return "salvar minuta";
	}

}

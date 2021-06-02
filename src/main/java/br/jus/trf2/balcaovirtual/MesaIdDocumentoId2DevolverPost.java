package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdDocumentoId2DevolverPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameMesaIdDocumentoId2DevolverPost;

public class MesaIdDocumentoId2DevolverPost implements IMesaIdDocumentoId2DevolverPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameMesaIdDocumentoId2DevolverPost.Request q = new IUsuarioUsernameMesaIdDocumentoId2DevolverPost.Request();
		q.lembrete = req.lembrete;

		Future<SwaggerAsyncResponse<IUsuarioUsernameMesaIdDocumentoId2DevolverPost.Response>> future = SwaggerCall
				.callAsync(
						getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocUrl(req.sistema)
								+ "/usuario/" + u.usuario + "/mesa/null/documento/" + req.id2 + "/devolver",
						q, IUsuarioUsernameMesaIdDocumentoId2DevolverPost.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameMesaIdDocumentoId2DevolverPost.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameMesaIdDocumentoId2DevolverPost.Response r = (IUsuarioUsernameMesaIdDocumentoId2DevolverPost.Response) sar
				.getResp();

		resp.status = r.status;
	}

	@Override
	public String getContext() {
		return "devolver minuta";
	}

}

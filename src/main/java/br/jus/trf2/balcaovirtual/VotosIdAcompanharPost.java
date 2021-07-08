package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IVotosIdAcompanharPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameMesaIdDocumentoId2SalvarPost;

public class VotosIdAcompanharPost implements IVotosIdAcompanharPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Request q = new IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Request();

		Future<SwaggerAsyncResponse<IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Response>> future = SwaggerCall
				.callAsync(
						getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocUrl(req.sistema)
								+ "/usuario/" + u.usuario + "/votos/" + req.id + "/acompanhar",
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
		return "acompanhar voto";
	}

}

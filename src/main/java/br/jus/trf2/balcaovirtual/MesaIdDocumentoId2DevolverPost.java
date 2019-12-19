package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdDocumentoId2DevolverPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdDocumentoId2DevolverPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdDocumentoId2DevolverPostResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameMesaIdDocumentoId2DevolverPostRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameMesaIdDocumentoId2DevolverPostResponse;

public class MesaIdDocumentoId2DevolverPost implements IMesaIdDocumentoId2DevolverPost {

	@Override
	public void run(MesaIdDocumentoId2DevolverPostRequest req, MesaIdDocumentoId2DevolverPostResponse resp)
			throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		UsuarioUsernameMesaIdDocumentoId2DevolverPostRequest q = new UsuarioUsernameMesaIdDocumentoId2DevolverPostRequest();
		q.lembrete = req.lembrete;

		Future<SwaggerAsyncResponse<UsuarioUsernameMesaIdDocumentoId2DevolverPostResponse>> future = SwaggerCall
				.callAsync(
						getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocUrl(req.sistema)
								+ "/usuario/" + u.usuario + "/mesa/null/documento/" + req.id2 + "/devolver",
						q, UsuarioUsernameMesaIdDocumentoId2DevolverPostResponse.class);
		SwaggerAsyncResponse<UsuarioUsernameMesaIdDocumentoId2DevolverPostResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioUsernameMesaIdDocumentoId2DevolverPostResponse r = (UsuarioUsernameMesaIdDocumentoId2DevolverPostResponse) sar
				.getResp();

		resp.status = r.status;
	}

	@Override
	public String getContext() {
		return "devolver minuta";
	}

}

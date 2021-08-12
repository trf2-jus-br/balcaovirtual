package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IVotosIdPedirVistaPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameVotosIdPedirVistaPost;

public class VotosIdPedirVistaPost implements IVotosIdPedirVistaPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameVotosIdPedirVistaPost.Request q = new IUsuarioUsernameVotosIdPedirVistaPost.Request();

		Future<SwaggerAsyncResponse<IUsuarioUsernameVotosIdPedirVistaPost.Response>> future = SwaggerCall.callAsync(
				getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocVotosUrl(req.sistema)
						+ "/pedir_vista?sigla=" + u.usuario + "&id_sessao_item=" + req.id,
				q, IUsuarioUsernameVotosIdPedirVistaPost.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameVotosIdPedirVistaPost.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameVotosIdPedirVistaPost.Response r = (IUsuarioUsernameVotosIdPedirVistaPost.Response) sar
				.getResp();

		resp.status = r.status;
	}

	@Override
	public String getContext() {
		return "divergir de voto";
	}

}

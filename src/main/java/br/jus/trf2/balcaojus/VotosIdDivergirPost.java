package br.jus.trf2.balcaojus;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IVotosIdDivergirPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameVotosIdDivergirPost;

public class VotosIdDivergirPost implements IVotosIdDivergirPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameVotosIdDivergirPost.Request q = new IUsuarioUsernameVotosIdDivergirPost.Request();

		Future<SwaggerAsyncResponse<IUsuarioUsernameVotosIdDivergirPost.Response>> future = SwaggerCall.callAsync(
				getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocVotosUrl(req.sistema)
						+ "/acompanha_divergencia?sigla=" + u.usuario + "&id_sessao_item=" + req.id,
				q, IUsuarioUsernameVotosIdDivergirPost.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameVotosIdDivergirPost.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameVotosIdDivergirPost.Response r = (IUsuarioUsernameVotosIdDivergirPost.Response) sar.getResp();

		resp.status = r.status;
	}

	@Override
	public String getContext() {
		return "divergir de voto";
	}

}

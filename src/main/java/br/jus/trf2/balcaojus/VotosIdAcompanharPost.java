package br.jus.trf2.balcaojus;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IVotosIdAcompanharPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameMesaIdDocumentoId2SalvarPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameVotosIdAcompanharPost;

public class VotosIdAcompanharPost implements IVotosIdAcompanharPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Request q = new IUsuarioUsernameMesaIdDocumentoId2SalvarPost.Request();

		Future<SwaggerAsyncResponse<IUsuarioUsernameVotosIdAcompanharPost.Response>> future = SwaggerCall.callAsync(
				getContext(), Utils.getApiEprocPassword(req.sistema), "POST", Utils.getApiEprocVotosUrl(req.sistema)
						+ "/acompanha_relator?sigla=" + u.usuario + "&id_sessao_item=" + req.id,
				q, IUsuarioUsernameVotosIdAcompanharPost.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameVotosIdAcompanharPost.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameVotosIdAcompanharPost.Response r = (IUsuarioUsernameVotosIdAcompanharPost.Response) sar
				.getResp();

		resp.status = r.status;
		resp.voto = VotosGet.buildVoto(req.sistema, r.voto);
	}

	@Override
	public String getContext() {
		return "acompanhar voto";
	}

}

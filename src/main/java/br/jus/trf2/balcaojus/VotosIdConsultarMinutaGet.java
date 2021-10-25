package br.jus.trf2.balcaojus;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IVotosIdConsultarMinutaGet.Request;
import br.jus.trf2.balcaojus.IBalcaojus.IVotosIdConsultarMinutaGet.Response;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameVotosIdConsultarMinutaGet;

public class VotosIdConsultarMinutaGet implements IVotosIdConsultarMinutaGet{
	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameVotosIdConsultarMinutaGet.Request q = new IUsuarioUsernameVotosIdConsultarMinutaGet.Request();

		Future<SwaggerAsyncResponse<IUsuarioUsernameVotosIdConsultarMinutaGet.Response>> future = SwaggerCall.callAsync(
				getContext(), Utils.getApiEprocPassword(req.sistema), "GET", Utils.getApiEprocVotosUrl(req.sistema)
						+ "/minuta?idminuta=" + req.idMinuta,
				q, IUsuarioUsernameVotosIdConsultarMinutaGet.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameVotosIdConsultarMinutaGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameVotosIdConsultarMinutaGet.Response r = (IUsuarioUsernameVotosIdConsultarMinutaGet.Response) sar
				.getResp();

		resp.status = r.status;
		resp.minuta = r.minuta;
	}

	@Override
	public String getContext() {
		return "consultar minuta";
	}

}

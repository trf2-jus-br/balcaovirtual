package br.jus.trf2.balcaojus;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IVotosIdConsultarMinutaGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameVotosIdMinutasId2Get;

public class VotosIdConsultarMinutaGet implements IVotosIdConsultarMinutaGet {
	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		IUsuarioUsernameVotosIdMinutasId2Get.Request q = new IUsuarioUsernameVotosIdMinutasId2Get.Request();
		Future<SwaggerAsyncResponse<IUsuarioUsernameVotosIdMinutasId2Get.Response>> future = SwaggerCall.callAsync(
				getContext(), Utils.getApiEprocPassword(req.sistema), "GET",
				Utils.getApiEprocVotosUrl(req.sistema) + "/minuta?idminuta=" + req.idminuta, q,
				IUsuarioUsernameVotosIdMinutasId2Get.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameVotosIdMinutasId2Get.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameVotosIdMinutasId2Get.Response r = (IUsuarioUsernameVotosIdMinutasId2Get.Response) sar.getResp();

		resp.status = r.status;
		resp.html = r.html;
	}

	@Override
	public String getContext() {
		return "consultar minuta";
	}

}

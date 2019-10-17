package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroConsultarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameProcessoNumeroConsultarGetResponse;

public class ProcessoNumeroConsultarGet implements IProcessoNumeroConsultarGet {

	@Override
	public void run(ProcessoNumeroConsultarGetRequest req, ProcessoNumeroConsultarGetResponse resp) throws Exception {
		String usuario = null;
		String senha = null;

		Usuario u = null;
		try {
			u = SessionsCreatePost.assertUsuario();
			UsuarioDetalhe detalhe = u.usuarios.get(req.sistema);
			if (detalhe != null) {
				usuario = u.usuario;
				senha = u.senha;
			}
		} catch (Exception ex) {
		}

		if (usuario == null && (ProcessoNumeroValidarGet.isValidToken(req.token, req.numero) || u != null)) {
			usuario = SwaggerServlet.getProperty("public.username");
			senha = SwaggerServlet.getProperty("public.password");
		}

		if (usuario == null)
			throw new PresentableUnloggedException("Usuário não possui login válido no sistema "
					+ Utils.getName(req.sistema) + " e também não passou pelo captcha");

		String json = SoapMNI.consultarProcesso(usuario, senha, req.sistema, req.numero, true, true, true);

		if (req.sistema.contains(".eproc"))
			json = enhanceEproc(usuario, req.sistema, req.numero, json);

		byte[] ba = json.getBytes(StandardCharsets.UTF_8);
		resp.inputstream = new ByteArrayInputStream(ba);
		resp.contentlength = (long) ba.length;
		resp.contenttype = "application/json";
	}

	private String enhanceEproc(String usuario, String sistema, String numero, String json) throws Exception {
		Future<SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroConsultarGetResponse>> future = SwaggerCall.callAsync(
				"obter tipos de petição intercorrente", Utils.getApiPassword(sistema), "GET",
				Utils.getApiUrl(sistema) + "/usuario/" + usuario + "/processo/" + numero + "/consultar", null,
				UsuarioUsernameProcessoNumeroConsultarGetResponse.class);
		SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroConsultarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioUsernameProcessoNumeroConsultarGetResponse r = (UsuarioUsernameProcessoNumeroConsultarGetResponse) sar
				.getResp();

		if (r.numero == null)
			return json;

		JsonParser parser = new JsonParser();
		JsonElement e = parser.parse(json);
		JsonObject op = e.getAsJsonObject().getAsJsonObject("value").getAsJsonObject("dadosBasicos")
				.getAsJsonObject("outroParametro");
		op.addProperty("nomeMagistrado", r.magistrado);
		json = e.toString();
		return json;
	}

	@Override
	public String getContext() {
		return "consultar processo";
	}

}

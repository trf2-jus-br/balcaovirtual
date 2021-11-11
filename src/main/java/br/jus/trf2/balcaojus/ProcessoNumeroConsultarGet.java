package br.jus.trf2.balcaojus;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroConsultarGet;
import br.jus.trf2.balcaojus.util.AcessoPublicoEPrivado;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameProcessoNumeroConsultarGet;

@AcessoPublicoEPrivado
public class ProcessoNumeroConsultarGet implements IProcessoNumeroConsultarGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		String usuario = null;
		String senha = null;
		String origem = null;

		Usuario u = null;
		try {
			u = AutenticarPost.assertUsuario();
			UsuarioDetalhe detalhe = u.usuarios.get(req.sistema);
			if (detalhe != null) {
				usuario = u.usuario;
				senha = u.getSenha();
				origem = detalhe.origem;
			}
		} catch (Exception ex) {
		}

		if (usuario == null && (ProcessoValidarGet.isValidToken(req.token, req.numero) || u != null)) {
			usuario = BalcaojusServlet.INSTANCE.getProperty("public.username");
			senha = BalcaojusServlet.INSTANCE.getProperty("public.password");
			origem = "pub";
		}

		if (usuario == null)
			throw new PresentableUnloggedException("Usuário não possui login válido no sistema "
					+ Utils.getName(req.sistema) + " e também não passou pelo captcha");

		usuario = Utils.preprocessarId(usuario, senha, req.sistema, origem);
		senha = Utils.preprocessarSenha(usuario, senha, req.sistema, origem);
		String json = SoapMNI.consultarProcesso(usuario, senha, req.sistema, req.numero, true, true, true);

		if (req.sistema.contains(".eproc"))
			json = enhanceEproc(usuario, req.sistema, req.numero, json);

		if (req.sistema.contains(".apolo"))
			json = enhanceApolo(usuario, req.sistema, req.numero, json);

		byte[] ba = json.getBytes(StandardCharsets.UTF_8);
		resp.inputstream = new ByteArrayInputStream(ba);
		resp.contentlength = (long) ba.length;
		resp.contenttype = "application/json";
	}

	private String enhanceEproc(String usuario, String sistema, String numero, String json) throws Exception {
		Future<SwaggerAsyncResponse<IUsuarioUsernameProcessoNumeroConsultarGet.Response>> future = SwaggerCall
				.callAsync("obter tipos de petição intercorrente", Utils.getApiPassword(sistema), "GET",
						Utils.getApiUrl(sistema) + "/usuario/" + usuario + "/processo/" + numero + "/consultar", null,
						IUsuarioUsernameProcessoNumeroConsultarGet.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameProcessoNumeroConsultarGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameProcessoNumeroConsultarGet.Response r = (IUsuarioUsernameProcessoNumeroConsultarGet.Response) sar
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

	private String enhanceApolo(String usuario, String sistema, String numero, String json) throws Exception {
		if (!Utils.isConsultaPublica(usuario))
			return json;
		JsonParser parser = new JsonParser();
		JsonElement e = parser.parse(json);
		JsonArray docs = e.getAsJsonObject().getAsJsonObject("value").getAsJsonArray("documento");
		if (docs == null)
			return json;
		Map<String, JsonElement> toRemove = new HashMap<>();
		for (JsonElement el : docs) {
			JsonObject doc = el.getAsJsonObject();
			if (doc.get("nivelSigilo").getAsInt() > 0)
				toRemove.put(doc.get("idDocumento").getAsString(), el);
		}
		for (JsonElement el : toRemove.values())
			docs.remove(el);
		JsonArray movs = e.getAsJsonObject().getAsJsonObject("value").getAsJsonArray("movimento");
		for (JsonElement el : movs) {
			JsonObject mov = el.getAsJsonObject();
			if (mov.get("idDocumentoVinculado") != null) {
				JsonArray vincs = mov.get("idDocumentoVinculado").getAsJsonArray();
				if (vincs == null)
					continue;
				for (int i = 0; i < vincs.size(); i++) {
					if (toRemove.containsKey(vincs.get(i).getAsString())) {
						vincs.remove(i);
						i--;
					}
				}
				if (vincs.size() == 0)
					mov.remove("idDocumentoVinculado");
			}
		}
		json = e.toString();
		return json;
	}

	@Override
	public String getContext() {
		return "consultar processo";
	}

}

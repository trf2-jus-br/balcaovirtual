package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroConsultarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ProcessoNumeroConsultarGet implements IProcessoNumeroConsultarGet {

	@Override
	public void run(ProcessoNumeroConsultarGetRequest req, ProcessoNumeroConsultarGetResponse resp) throws Exception {
		String usuario;
		String senha;

		if (ProcessoNumeroValidarGet.assertValidToken(req.token, req.numero)) {
			usuario = SwaggerServlet.getProperty("public.username");
			senha = SwaggerServlet.getProperty("public.password");
		} else {
			Usuario u;
			u = SessionsCreatePost.assertUsuario();
			usuario = u.usuario;
			senha = u.senha;
		}
		String json = SoapMNI.consultarProcesso(usuario, senha, req.sistema, req.numero, true, true, true);
		byte[] ba = json.getBytes(StandardCharsets.UTF_8);
		resp.inputstream = new ByteArrayInputStream(ba);
		resp.contentlength = (long) ba.length;
		resp.contenttype = "application/json";
	}

	@Override
	public String getContext() {
		return "consultar processo";
	}

}

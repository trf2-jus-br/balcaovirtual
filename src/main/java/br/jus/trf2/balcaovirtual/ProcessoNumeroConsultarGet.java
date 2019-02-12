package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroConsultarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroConsultarGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ProcessoNumeroConsultarGet implements IProcessoNumeroConsultarGet {

	@Override
	public void run(ProcessoNumeroConsultarGetRequest req, ProcessoNumeroConsultarGetResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		Usuario u = SessionsCreatePost.assertUsuario();
		String json = SoapMNI.consultarProcesso(u.usuario, u.senha, req.orgao, req.numero, true, true, true);
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

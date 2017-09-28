package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroValidarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetResponse;

public class ProcessoNumeroValidarGet implements IProcessoNumeroValidarGet {

	@Override
	public void run(ProcessoNumeroValidarGetRequest req, ProcessoNumeroValidarGetResponse resp) throws Exception {
		String url = null;
		try {
			Usuario u = SessionsCreatePost.assertUsuario();
			if (u.isInterno())
				url = "/usuario/" + u.usuario + "/processo/" + req.numero;
			else
				url = "/usuario-web/" + u.usuario + "/processo/" + req.numero;
		} catch (Exception e) {
			url = "/processo/validar/" + req.numero;

		}
		ProcessoValidarNumeroGetRequest q = new ProcessoValidarNumeroGetRequest();
		q.numero = req.numero;

		Future<SwaggerAsyncResponse<ProcessoValidarNumeroGetResponse>> future = SwaggerCall.callAsync(
				"validar número de processo", null, "GET", Utils.getWsProcessualUrl() + url, null,
				ProcessoValidarNumeroGetResponse.class);
		SwaggerAsyncResponse<ProcessoValidarNumeroGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		ProcessoValidarNumeroGetResponse r = (ProcessoValidarNumeroGetResponse) sar.getResp();
		// if ("TRF - 2a Região".equals(r.orgao))
		// r.orgao = "TRF2";
		// if ("Seção Judiciária do RJ".equals(r.orgao))
		// r.orgao = "JFRJ";
		// if ("Seção Judiciária do ES".equals(r.orgao))
		// r.orgao = "JFES";

		resp.numero = r.numero;
		resp.orgao = r.orgao;
		resp.unidade = r.unidade != null ? r.unidade.trim() : null;
		resp.localnaunidade = r.localNaUnidade;
		resp.segredodejustica = r.segredodejustica;
		resp.segredodejusticadesistema = r.segredodejusticadesistema;
		resp.segredodejusticaabsoluto = r.segredodejusticaabsoluto;
		resp.usuarioautorizado = r.usuarioautorizado;
		resp.digital = r.eletronico;
		resp.sentenciado = r.sentenciado;
		resp.baixado = r.baixado;
		resp.cdas = r.cdas;
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

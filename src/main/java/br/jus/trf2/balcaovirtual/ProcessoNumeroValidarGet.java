package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroValidarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetResponse;

public class ProcessoNumeroValidarGet implements IProcessoNumeroValidarGet {

	@Override
	public void run(ProcessoNumeroValidarGetRequest req, ProcessoNumeroValidarGetResponse resp) throws Exception {
		ProcessoValidarNumeroGetRequest q = new ProcessoValidarNumeroGetRequest();
		q.numero = req.numero;
		Future<SwaggerAsyncResponse<ProcessoValidarNumeroGetResponse>> future = SwaggerCall.callAsync(
				"validar número de processo", null, "GET",
				Utils.getWsProcessualUrl() + "/processo/validar/" + req.numero, null,
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
		resp.unidade = r.unidade;
		resp.segredodejustica = r.segredodejustica;
		resp.segredodejusticadesistema = r.segredodejusticadesistema;
		resp.segredodejusticaabsoluto = r.segredodejusticaabsoluto;
		resp.digital = r.eletronico;
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

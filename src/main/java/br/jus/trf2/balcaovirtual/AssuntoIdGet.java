package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AssuntoIdGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AssuntoIdGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAssuntoIdGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.AssuntoCnjIdGetResponse;

public class AssuntoIdGet implements IAssuntoIdGet {
	private static final Logger log = LoggerFactory.getLogger(AssuntoIdGet.class);

	@Override
	public void run(AssuntoIdGetRequest req, AssuntoIdGetResponse resp) throws Exception {
		Future<SwaggerAsyncResponse<AssuntoCnjIdGetResponse>> future = SwaggerCall.callAsync("obter classe processual",
				null, "GET", Utils.getWsProcessualUrl() + "/assunto-cnj/" + req.id + "?orgao=" + req.orgao, null,
				AssuntoCnjIdGetResponse.class);
		SwaggerAsyncResponse<AssuntoCnjIdGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		AssuntoCnjIdGetResponse r = (AssuntoCnjIdGetResponse) sar.getResp();

		resp.codigo = r.codigo;
		resp.descricao = r.descricao.split(" - ")[0];
		resp.descricaocompleta = r.descricao;
		resp.ativo = r.ativo;
	}

	@Override
	public String getContext() {
		return "obter classe processual";
	}

}

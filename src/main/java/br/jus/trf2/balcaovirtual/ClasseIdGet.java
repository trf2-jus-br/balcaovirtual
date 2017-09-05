package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ClasseIdGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ClasseIdGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IClasseIdGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ClasseCnjIdGetResponse;

public class ClasseIdGet implements IClasseIdGet {

	@Override
	public void run(ClasseIdGetRequest req, ClasseIdGetResponse resp) throws Exception {
		Future<SwaggerAsyncResponse<ClasseCnjIdGetResponse>> future = SwaggerCall.callAsync("obter classe processual",
				null, "GET", Utils.getWsProcessualUrl() + "/classe-cnj/" + req.id + "?orgao=" + req.orgao, null,
				ClasseCnjIdGetResponse.class);
		SwaggerAsyncResponse<ClasseCnjIdGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		ClasseCnjIdGetResponse r = (ClasseCnjIdGetResponse) sar.getResp();

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

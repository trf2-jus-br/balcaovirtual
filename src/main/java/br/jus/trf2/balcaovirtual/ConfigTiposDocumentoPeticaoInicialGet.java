package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigTiposDocumentoPeticaoInicialGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.TipoDocumentoPeticaoInicial;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ILocalidadeGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;

public class ConfigTiposDocumentoPeticaoInicialGet implements IConfigTiposDocumentoPeticaoInicialGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Future<SwaggerAsyncResponse<ILocalidadeGet.Response>> future = SwaggerCall.callAsync(
				"obter tipos de documento para petição inicial", Utils.getApiPassword(req.sistema), "GET",
				Utils.getApiUrl(req.sistema) + "/peticao/inicial/tipos-documento", null, ILocalidadeGet.Response.class);
		SwaggerAsyncResponse<ILocalidadeGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		ILocalidadeGet.Response r = (ILocalidadeGet.Response) sar.getResp();

		resp.list = new ArrayList<>();
		for (IdNome idNome : r.list) {
			TipoDocumentoPeticaoInicial o = new TipoDocumentoPeticaoInicial();
			o.id = idNome.id;
			o.nome = idNome.nome;
			resp.list.add(o);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de tipos de documento para petição inicial";
	}

}

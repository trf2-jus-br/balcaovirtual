package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigTiposDocumentoPeticaoInicialGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigTiposDocumentoPeticaoInicialGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigTiposDocumentoPeticaoInicialGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Localidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.TipoDocumentoPeticaoInicial;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.LocalidadeGetResponse;

public class ConfigTiposDocumentoPeticaoInicialGet implements IConfigTiposDocumentoPeticaoInicialGet {

	@Override
	public void run(ConfigTiposDocumentoPeticaoInicialGetRequest req,
			ConfigTiposDocumentoPeticaoInicialGetResponse resp) throws Exception {
		AutenticarPost.assertAuthorization();

		Future<SwaggerAsyncResponse<LocalidadeGetResponse>> future = SwaggerCall.callAsync(
				"obter tipos de documento para petição inicial", Utils.getApiPassword(req.sistema), "GET",
				Utils.getApiUrl(req.sistema) + "/peticao/inicial/tipos-documento", null, LocalidadeGetResponse.class);
		SwaggerAsyncResponse<LocalidadeGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		LocalidadeGetResponse r = (LocalidadeGetResponse) sar.getResp();

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

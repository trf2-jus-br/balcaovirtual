package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPeticaoIntercorrenteContarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteContarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteContarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.QuantidadePorData;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Contagem;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse;

public class PeticaoIntercorrenteContarGet implements IPeticaoIntercorrenteContarGet {

	@Override
	public void run(PeticaoIntercorrenteContarGetRequest req, PeticaoIntercorrenteContarGetResponse resp)
			throws Exception {

		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Future<SwaggerAsyncResponse<UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse>> future = SwaggerCall
				.callAsync("obter tipos de petição intercorrente", null, "GET",
						Utils.getWsProcessualUrl() + "/usuario-web/" + jwt.get("username")
								+ "/peticao-intercorrente/contar?dias=7",
						null, UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse.class);
		SwaggerAsyncResponse<UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse r = (UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse) sar
				.getResp();

		resp.list = new ArrayList<>();
		if (r.list != null)
			for (Contagem i : r.list) {
				QuantidadePorData t = new QuantidadePorData();
				t.data = i.data;
				t.quantidade = i.quantidade;
				resp.list.add(t);
			}
	}

	@Override
	public String getContext() {
		return "obter quantidade de petições intercorrentes nos últimos 7 dias";
	}

}

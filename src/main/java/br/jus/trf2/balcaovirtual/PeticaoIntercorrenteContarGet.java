package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPeticaoIntercorrenteContarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteContarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteContarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.QuantidadePorData;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Contagem;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernamePeticaoIntercorrenteContarGetResponse;

public class PeticaoIntercorrenteContarGet implements IPeticaoIntercorrenteContarGet {

	@Override
	public void run(PeticaoIntercorrenteContarGetRequest req, PeticaoIntercorrenteContarGetResponse resp)
			throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter tipos de petição intercorrente", Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + jwt.get("username")
									+ "/peticao-intercorrente/contar?dias=7",
							null, UsuarioUsernamePeticaoIntercorrenteContarGetResponse.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, SessionsCreatePost.TIMEOUT_MILLISECONDS);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			UsuarioUsernamePeticaoIntercorrenteContarGetResponse r = (UsuarioUsernamePeticaoIntercorrenteContarGetResponse) mcr.responses
					.get(system);
			if (r.list != null)
				for (Contagem i : r.list) {
					QuantidadePorData t = new QuantidadePorData();
					t.data = i.data;
					t.quantidade = i.quantidade;
					resp.list.add(t);
				}
		}
	}

	@Override
	public String getContext() {
		return "obter quantidade de petições intercorrentes nos últimos 7 dias";
	}

}

package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPeticaoIntercorrenteListarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteListarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteListarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteResumo;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.PeticaoIntercorrente;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernamePeticaoIntercorrenteListarGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernamePeticaoIntercorrenteListarGetResponse;

public class PeticaoIntercorrenteListarGet implements IPeticaoIntercorrenteListarGet {

	@Override
	public void run(PeticaoIntercorrenteListarGetRequest req, PeticaoIntercorrenteListarGetResponse resp)
			throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		UsuarioUsernamePeticaoIntercorrenteListarGetRequest q = new UsuarioUsernamePeticaoIntercorrenteListarGetRequest();
		q.data = req.data;
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter resumos de petições intercorrentes",
							Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + jwt.get("username")
									+ "/peticao-intercorrente/listar",
							q, UsuarioUsernamePeticaoIntercorrenteListarGetResponse.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, SessionsCreatePost.TIMEOUT_MILLISECONDS);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			UsuarioUsernamePeticaoIntercorrenteListarGetResponse r = (UsuarioUsernamePeticaoIntercorrenteListarGetResponse) mcr.responses
					.get(system);
			if (r.list != null)
				for (PeticaoIntercorrente i : r.list) {
					PeticaoIntercorrenteResumo t = new PeticaoIntercorrenteResumo();
					t.processo = i.processo;
					t.protocolo = i.protocolo;
					t.dataprotocolo = i.dataprotocolo;
					t.classe = i.classe;
					t.sistema = system;
					t.unidade = i.unidade;
					resp.list.add(t);
				}
		}
	}

	@Override
	public String getContext() {
		return "obter resumo de petições intercorrentes";
	}

}

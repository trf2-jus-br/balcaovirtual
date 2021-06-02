package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPeticaoIntercorrenteContarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.QuantidadePorData;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Contagem;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernamePeticaoIntercorrenteContarGet;

public class PeticaoIntercorrenteContarGet implements IPeticaoIntercorrenteContarGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter tipos de petição intercorrente",
							Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + u.usuario + "/peticao-intercorrente/contar?dias=7",
							null, IUsuarioUsernamePeticaoIntercorrenteContarGet.Response.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, BalcaoVirtualServlet.TIMEOUT_MILLISECONDS);
		resp.status = Utils.getStatus(mcr);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			IUsuarioUsernamePeticaoIntercorrenteContarGet.Response r = (IUsuarioUsernamePeticaoIntercorrenteContarGet.Response) mcr.responses
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

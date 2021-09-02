package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IPeticaoIntercorrenteListarGet;
import br.jus.trf2.balcaojus.IBalcaojus.PeticaoIntercorrenteResumo;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernamePeticaoIntercorrenteListarGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.PeticaoIntercorrente;

public class PeticaoIntercorrenteListarGet implements IPeticaoIntercorrenteListarGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		IUsuarioUsernamePeticaoIntercorrenteListarGet.Request q = new IUsuarioUsernamePeticaoIntercorrenteListarGet.Request();
		q.data = req.data;
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter resumos de petições intercorrentes",
							Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + u.usuario + "/peticao-intercorrente/listar", q,
							IUsuarioUsernamePeticaoIntercorrenteListarGet.Response.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, BalcaojusServlet.TIMEOUT_MILLISECONDS);
		resp.status = Utils.getStatus(mcr);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			IUsuarioUsernamePeticaoIntercorrenteListarGet.Response r = (IUsuarioUsernamePeticaoIntercorrenteListarGet.Response) mcr.responses
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

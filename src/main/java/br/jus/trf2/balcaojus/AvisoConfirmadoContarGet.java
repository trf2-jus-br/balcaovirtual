package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaojus.IBalcaojus.IAvisoConfirmadoContarGet;
import br.jus.trf2.balcaojus.IBalcaojus.QuantidadeConfirmadaPorData;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameAvisoConfirmadoContarGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.QuantidadeConfirmada;

public class AvisoConfirmadoContarGet implements IAvisoConfirmadoContarGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Map<String, Object> jwt = AutenticarPost.assertUsuarioAutorizado();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter quantidade de avisos confirmado",
							Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + jwt.get("username")
									+ "/aviso-confirmado/contar?dias=100",
							null, IUsuarioUsernameAvisoConfirmadoContarGet.Response.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, BalcaojusServlet.TIMEOUT_MILLISECONDS);
		resp.status = Utils.getStatus(mcr);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			IUsuarioUsernameAvisoConfirmadoContarGet.Response r = (IUsuarioUsernameAvisoConfirmadoContarGet.Response) mcr.responses
					.get(system);
			if (r.list != null)
				for (QuantidadeConfirmada i : r.list) {
					QuantidadeConfirmadaPorData t = new QuantidadeConfirmadaPorData();
					t.data = i.data;
					t.quantidadedousuarioporconfirmacao = i.quantidadeDoUsuarioPorConfirmacao;
					t.quantidadedousuarioporomissao = i.quantidadeDoUsuarioPorOmissao;
					t.quantidadedogrupoporconfirmacao = i.quantidadeDoGrupoPorConfirmacao;
					t.quantidadedogrupoporomissao = i.quantidadeDoGrupoPorOmissao;
					resp.list.add(t);
				}
		}
	}

	@Override
	public String getContext() {
		return "obter quantidade de avisos confirmados nos últimos 7 dias";
	}

}

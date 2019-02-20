package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoContarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoContarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoConfirmadoContarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.QuantidadeConfirmadaPorData;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.QuantidadeConfirmada;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernameAvisoConfirmadoContarGetResponse;

public class AvisoConfirmadoContarGet implements IAvisoConfirmadoContarGet {

	@Override
	public void run(AvisoConfirmadoContarGetRequest req, AvisoConfirmadoContarGetResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter quantidade de avisos confirmado",
							"Bearer " + authorization, "GET",
							Utils.getApiUrl(system) + "/usuario-web/" + jwt.get("username")
									+ "/aviso-confirmado/contar?dias=100",
							null, UsuarioWebUsernameAvisoConfirmadoContarGetResponse.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, SessionsCreatePost.TIMEOUT_MILLISECONDS);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			UsuarioWebUsernameAvisoConfirmadoContarGetResponse r = (UsuarioWebUsernameAvisoConfirmadoContarGetResponse) mcr.responses
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

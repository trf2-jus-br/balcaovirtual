package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

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

		Future<SwaggerAsyncResponse<UsuarioWebUsernameAvisoConfirmadoContarGetResponse>> future = SwaggerCall.callAsync(
				"obter quantidade de avisos confirmado", "Bearer " + authorization, "GET",
				Utils.getWsProcessualUrl() + "/usuario-web/" + jwt.get("username")
						+ "/aviso-confirmado/contar?dias=100",
				null, UsuarioWebUsernameAvisoConfirmadoContarGetResponse.class);
		SwaggerAsyncResponse<UsuarioWebUsernameAvisoConfirmadoContarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioWebUsernameAvisoConfirmadoContarGetResponse r = (UsuarioWebUsernameAvisoConfirmadoContarGetResponse) sar
				.getResp();

		resp.list = new ArrayList<>();
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

	@Override
	public String getContext() {
		return "obter quantidade de avisos confirmados nos últimos 7 dias";
	}

}

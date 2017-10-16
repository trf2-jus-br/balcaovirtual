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
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.QuantidadePorData;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.Contagem;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse;

public class AvisoConfirmadoContarGet implements IAvisoConfirmadoContarGet {

	@Override
	public void run(AvisoConfirmadoContarGetRequest req, AvisoConfirmadoContarGetResponse resp) throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		// Future<SwaggerAsyncResponse<UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse>>
		// future = SwaggerCall
		// .callAsync("obter tipos de petição intercorrente", null, "GET",
		// Utils.getWsProcessualUrl() + "/usuario-web/" + jwt.get("username")
		// + "/peticao-intercorrente/contar?dias=7",
		// null, UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse.class);
		// SwaggerAsyncResponse<UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse>
		// sar = future.get();
		// if (sar.getException() != null)
		// throw sar.getException();
		// UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse r =
		// (UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse) sar
		// .getResp();

		resp.list = new ArrayList<>();
		// if (r.list != null)
		// for (Contagem i : r.list) {
		// QuantidadePorData t = new QuantidadePorData();
		// t.data = i.data;
		// t.quantidade = i.quantidade;
		// resp.list.add(t);
		// }
		QuantidadeConfirmadaPorData t = new QuantidadeConfirmadaPorData();
		t.data = "2017-10-13";
		t.quantidadeDoUsuarioPorConfirmacao = "48";
		t.quantidadeDoUsuarioPorOmissao = "5";
		t.quantidadeDoGrupoPorConfirmacao = "1968";
		t.quantidadeDoGrupoPorOmissao = "12";
		resp.list.add(t);
	}

	@Override
	public String getContext() {
		return "obter quantidade de avisos confirmados nos últimos 7 dias";
	}

}

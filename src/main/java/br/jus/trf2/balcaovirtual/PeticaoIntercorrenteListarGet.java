package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPeticaoIntercorrenteListarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteListarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteListarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoIntercorrenteResumo;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.PeticaoIntercorrente;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse;

public class PeticaoIntercorrenteListarGet implements IPeticaoIntercorrenteListarGet {

	@Override
	public void run(PeticaoIntercorrenteListarGetRequest req, PeticaoIntercorrenteListarGetResponse resp)
			throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Future<SwaggerAsyncResponse<UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse>> future = SwaggerCall
				.callAsync("obter resumos de petições intercorrentes", null, "GET",
						Utils.getWsProcessualUrl() + "/usuario-web/" + jwt.get("username")
								+ "/peticao-intercorrente/listar?data=" + req.data,
						null, UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse.class);
		SwaggerAsyncResponse<UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse r = (UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse) sar
				.getResp();

		resp.list = new ArrayList<>();
		if (r.list != null)
			for (PeticaoIntercorrente i : r.list) {
				PeticaoIntercorrenteResumo t = new PeticaoIntercorrenteResumo();
				t.processo = i.processo;
				t.protocolo = i.protocolo;
				t.dataprotocolo = i.dataprotocolo;
				t.classe = i.classe;
				t.orgao = i.orgao;
				t.unidade = i.unidade;
				resp.list.add(t);
			}
	}

	@Override
	public String getContext() {
		return "obter resumo de petições intercorrentes";
	}

}

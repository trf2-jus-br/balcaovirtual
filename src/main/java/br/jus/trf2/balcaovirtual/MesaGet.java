package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Mesa;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameMesasGetResponse;

public class MesaGet implements IMesaGet {

	@Override
	public void run(MesaGetRequest req, MesaGetResponse resp) throws Exception {
//		String authorization = SessionsCreatePost.assertAuthorization();
//		Usuario u = SessionsCreatePost.assertUsuario();
//		if (!u.isInterno())
//			throw new Exception("Mesas só podem ser acessadas por usuários internos");
//
//		Future<SwaggerAsyncResponse<UsuarioUsernameMesasGetResponse>> future = SwaggerCall.callAsync(
//				"obter mesas virtuais", "Bearer " + authorization, "GET",
//				Utils.getWsProcessualUrl() + "/usuario/" + u.usuario + "/mesas", null,
//				UsuarioUsernameMesasGetResponse.class);
//		SwaggerAsyncResponse<UsuarioUsernameMesasGetResponse> sar = future.get();
//		if (sar.getException() != null)
//			throw sar.getException();
//		UsuarioUsernameMesasGetResponse r = (UsuarioUsernameMesasGetResponse) sar.getResp();
//
//		resp.list = new ArrayList<>();
//
//		if (r.list == null)
//			return;
//
//		for (br.jus.trf2.sistemaprocessual.ISistemaProcessual.Mesa rm : r.list) {
//			Mesa m = new Mesa();
//			m.id = rm.orgao + "_" + rm.idlocal + "_" + rm.idmesa;
//			m.nome = rm.nome;
//			resp.list.add(m);
//		}
	}

	@Override
	public String getContext() {
		return "obter classe processual";
	}

}

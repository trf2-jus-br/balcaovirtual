package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigTipoPeticaoIntercorrente;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPeticaoIntercorrenteTiposGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticaoIntercorrenteTiposGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticaoIntercorrenteTiposGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.TipoPeticaoIntercorrente;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameProcessoNumeroPeticaoIntercorrenteTiposGetResponse;

public class ProcessoNumeroPeticaoIntercorrenteTiposGet implements IProcessoNumeroPeticaoIntercorrenteTiposGet {

	@Override
	public void run(ProcessoNumeroPeticaoIntercorrenteTiposGetRequest req,
			ProcessoNumeroPeticaoIntercorrenteTiposGetResponse resp) throws Exception {
		SessionsCreatePost.assertAuthorization();
		Usuario u = SessionsCreatePost.assertUsuario();

		Future<SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroPeticaoIntercorrenteTiposGetResponse>> future = SwaggerCall
				.callAsync("obter tipos de petição intercorrente", null, "GET",
						Utils.getApiUrl(req.sistema) + "/usuario/" + u.usuario + "/processo/" + req.numero
								+ "/peticao-intercorrente/tipos",
						null, UsuarioUsernameProcessoNumeroPeticaoIntercorrenteTiposGetResponse.class);
		SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroPeticaoIntercorrenteTiposGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioUsernameProcessoNumeroPeticaoIntercorrenteTiposGetResponse r = (UsuarioUsernameProcessoNumeroPeticaoIntercorrenteTiposGetResponse) sar
				.getResp();

		if (r.list == null || r.list.size() == 0)
			return;

		resp.list = new ArrayList<>();
		for (TipoPeticaoIntercorrente t : r.list) {
			ConfigTipoPeticaoIntercorrente tpi = new ConfigTipoPeticaoIntercorrente();
			tpi.sistema = req.sistema;
			tpi.id = t.id;
			tpi.descricao = t.descricao;
			resp.list.add(tpi);
		}
	}

	@Override
	public String getContext() {
		return "obter tipos de petição intercorrente";
	}

}

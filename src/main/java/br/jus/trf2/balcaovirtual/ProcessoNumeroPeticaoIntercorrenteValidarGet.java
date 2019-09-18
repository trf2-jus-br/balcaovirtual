package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigTipoPeticaoIntercorrente;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPeticaoIntercorrenteTiposGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticaoIntercorrenteValidarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticaoIntercorrenteValidarGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.TipoPeticaoIntercorrente;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGetResponse;

public class ProcessoNumeroPeticaoIntercorrenteValidarGet implements IProcessoNumeroPeticaoIntercorrenteTiposGet {

	@Override
	public void run(ProcessoNumeroPeticaoIntercorrenteValidarGetRequest req,
			ProcessoNumeroPeticaoIntercorrenteValidarGetResponse resp) throws Exception {
		SessionsCreatePost.assertAuthorization();
		Usuario u = SessionsCreatePost.assertUsuario();

		Future<SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGetResponse>> future = SwaggerCall
				.callAsync("obter tipos de petição intercorrente", null, "GET",
						Utils.getApiUrl(req.sistema) + "/usuario/" + u.usuario + "/processo/" + req.numero
								+ "/peticao-intercorrente/validar",
						null, UsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGetResponse.class);
		SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGetResponse r = (UsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGetResponse) sar
				.getResp();

		if (r.tipos == null || r.tipos.size() == 0)
			return;

		resp.tipos = new ArrayList<>();
		for (TipoPeticaoIntercorrente t : r.tipos) {
			ConfigTipoPeticaoIntercorrente tpi = new ConfigTipoPeticaoIntercorrente();
			tpi.sistema = req.sistema;
			tpi.id = t.id;
			tpi.descricao = t.descricao;
			resp.tipos.add(tpi);
		}
		resp.identencerraprazos = r.identencerraprazos;
		resp.sigilo = r.sigilo;
		resp.parte = r.parte;
	}

	@Override
	public String getContext() {
		return "obter tipos de petição intercorrente";
	}

}

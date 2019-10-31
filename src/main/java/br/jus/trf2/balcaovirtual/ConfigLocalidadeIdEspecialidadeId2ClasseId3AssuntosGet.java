package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Assunto;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.LocalidadeIdEspecialidadeId2ClasseId3AssuntoGetResponse;

public class ConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGet
		implements IConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGet {

	@Override
	public void run(ConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGetRequest req,
			ConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGetResponse resp) throws Exception {
		AutenticarPost.assertAuthorization();

		Future<SwaggerAsyncResponse<LocalidadeIdEspecialidadeId2ClasseId3AssuntoGetResponse>> future = SwaggerCall
				.callAsync("obter classes", Utils.getApiPassword(req.sistema), "GET",
						Utils.getApiUrl(req.sistema) + "/localidade/" + req.id + "/especialidade/" + req.id2
								+ "/classe/" + req.id3.split("\\|")[1] + "/assunto",
						null, LocalidadeIdEspecialidadeId2ClasseId3AssuntoGetResponse.class);
		SwaggerAsyncResponse<LocalidadeIdEspecialidadeId2ClasseId3AssuntoGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		LocalidadeIdEspecialidadeId2ClasseId3AssuntoGetResponse r = (LocalidadeIdEspecialidadeId2ClasseId3AssuntoGetResponse) sar
				.getResp();

		resp.list = new ArrayList<>();
		for (IdNome idNome : r.list) {
			Assunto o = new Assunto();
			o.id = idNome.id;
			o.nome = idNome.nome;
			resp.list.add(o);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de classes";
	}

}

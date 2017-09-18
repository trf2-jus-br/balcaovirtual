package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Classe;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadeId2ClassesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadeId2ClassesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadeIdEspecialidadeId2ClassesGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.LocalidadeIdEspecialidadeId2ClasseGetResponse;

public class ConfigLocalidadeIdEspecialidadeId2ClassesGet implements IConfigLocalidadeIdEspecialidadeId2ClassesGet {

	@Override
	public void run(ConfigLocalidadeIdEspecialidadeId2ClassesGetRequest req,
			ConfigLocalidadeIdEspecialidadeId2ClassesGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		Future<SwaggerAsyncResponse<LocalidadeIdEspecialidadeId2ClasseGetResponse>> future = SwaggerCall.callAsync(
				"obter classes", null, "GET", Utils.getWsProcessualUrl() + "/localidade/" + req.id + "/especialidade/"
						+ req.id2 + "/classe?orgao=" + req.orgao,
				null, LocalidadeIdEspecialidadeId2ClasseGetResponse.class);
		SwaggerAsyncResponse<LocalidadeIdEspecialidadeId2ClasseGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		LocalidadeIdEspecialidadeId2ClasseGetResponse r = (LocalidadeIdEspecialidadeId2ClasseGetResponse) sar.getResp();

		resp.list = new ArrayList<>();
		for (IdNome idNome : r.list) {
			Classe o = new Classe();
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

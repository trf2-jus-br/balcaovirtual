package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Especialidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadeIdEspecialidadesGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.LocalidadeIdEspecialidadeGetResponse;

public class ConfigLocalidadeIdEspecialidadesGet implements IConfigLocalidadeIdEspecialidadesGet {

	@Override
	public void run(ConfigLocalidadeIdEspecialidadesGetRequest req, ConfigLocalidadeIdEspecialidadesGetResponse resp)
			throws Exception {
		SessionsCreatePost.assertAuthorization();

		Future<SwaggerAsyncResponse<LocalidadeIdEspecialidadeGetResponse>> future = SwaggerCall.callAsync(
				"obter especialidades", null, "GET",
				Utils.getApiUrl(req.sistema) + "/localidade/" + req.id + "/especialidade", null,
				LocalidadeIdEspecialidadeGetResponse.class);
		SwaggerAsyncResponse<LocalidadeIdEspecialidadeGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		LocalidadeIdEspecialidadeGetResponse r = (LocalidadeIdEspecialidadeGetResponse) sar.getResp();

		resp.list = new ArrayList<>();
		for (IdNome idNome : r.list) {
			Especialidade o = new Especialidade();
			o.id = idNome.id;
			o.nome = idNome.nome;
			resp.list.add(o);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de especialidades";
	}

}

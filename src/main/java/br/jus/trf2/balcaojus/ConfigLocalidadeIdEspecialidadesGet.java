package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.IBalcaojus.Especialidade;
import br.jus.trf2.balcaojus.IBalcaojus.IConfigLocalidadeIdEspecialidadesGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ILocalidadeIdEspecialidadeGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;

public class ConfigLocalidadeIdEspecialidadesGet implements IConfigLocalidadeIdEspecialidadesGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Future<SwaggerAsyncResponse<ILocalidadeIdEspecialidadeGet.Response>> future = SwaggerCall.callAsync(
				"obter especialidades", Utils.getApiPassword(req.sistema), "GET",
				Utils.getApiUrl(req.sistema) + "/localidade/" + req.id + "/especialidade", null,
				ILocalidadeIdEspecialidadeGet.Response.class);
		SwaggerAsyncResponse<ILocalidadeIdEspecialidadeGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		ILocalidadeIdEspecialidadeGet.Response r = (ILocalidadeIdEspecialidadeGet.Response) sar.getResp();

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

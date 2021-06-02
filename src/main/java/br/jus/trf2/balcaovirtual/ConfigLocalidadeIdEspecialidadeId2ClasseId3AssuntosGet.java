package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Assunto;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ILocalidadeIdEspecialidadeId2ClasseId3AssuntoGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNome;

public class ConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGet
		implements IConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Future<SwaggerAsyncResponse<ILocalidadeIdEspecialidadeId2ClasseId3AssuntoGet.Response>> future = SwaggerCall
				.callAsync("obter classes", Utils.getApiPassword(req.sistema), "GET",
						Utils.getApiUrl(req.sistema) + "/localidade/" + req.id + "/especialidade/" + req.id2
								+ "/classe/" + req.id3.split("\\|")[1] + "/assunto",
						null, ILocalidadeIdEspecialidadeId2ClasseId3AssuntoGet.Response.class);
		SwaggerAsyncResponse<ILocalidadeIdEspecialidadeId2ClasseId3AssuntoGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		ILocalidadeIdEspecialidadeId2ClasseId3AssuntoGet.Response r = (ILocalidadeIdEspecialidadeId2ClasseId3AssuntoGet.Response) sar
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

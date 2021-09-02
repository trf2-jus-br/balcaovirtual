package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.IBalcaojus.Classe;
import br.jus.trf2.balcaojus.IBalcaojus.IConfigLocalidadeIdEspecialidadeId2ClassesGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ILocalidadeIdEspecialidadeId2ClasseGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IdNomeClasseCNJ;

public class ConfigLocalidadeIdEspecialidadeId2ClassesGet implements IConfigLocalidadeIdEspecialidadeId2ClassesGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Future<SwaggerAsyncResponse<ILocalidadeIdEspecialidadeId2ClasseGet.Response>> future = SwaggerCall.callAsync(
				"obter classes", Utils.getApiPassword(req.sistema), "GET",
				Utils.getApiUrl(req.sistema) + "/localidade/" + req.id + "/especialidade/" + req.id2 + "/classe", null,
				ILocalidadeIdEspecialidadeId2ClasseGet.Response.class);
		SwaggerAsyncResponse<ILocalidadeIdEspecialidadeId2ClasseGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		ILocalidadeIdEspecialidadeId2ClasseGet.Response r = (ILocalidadeIdEspecialidadeId2ClasseGet.Response) sar
				.getResp();

		resp.list = new ArrayList<>();
		for (IdNomeClasseCNJ idNome : r.list) {
			if (idNome.classecnj == null || idNome.classecnj.codigo == null || idNome.classecnj.descricao == null)
				continue;
			Classe o = new Classe();
			o.id = Integer.toString(idNome.classecnj.codigo.intValue()) + "|" + idNome.id;
			// o.nome = idNome.classecnj.descricao;//.split(" - ")[0];
			o.nome = idNome.nome;// .split(" - ")[0];
			o.valordacausaobrigatorio = idNome.valordacausaobrigatorio;
			resp.list.add(o);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de classes";
	}

}

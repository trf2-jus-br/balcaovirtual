package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPeticaoIntercorrenteTiposGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPeticaoIntercorrenteTiposGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigTipoPeticaoIntercorrente;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigPeticaoIntercorrenteTiposGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.PeticaoIntercorrenteTiposGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.TipoPeticaoIntercorrente;

public class ConfigPeticaoIntercorrenteTiposGet implements IConfigPeticaoIntercorrenteTiposGet {

	@Override
	public void run(ConfigPeticaoIntercorrenteTiposGetRequest req, ConfigPeticaoIntercorrenteTiposGetResponse resp)
			throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter tipos de petição intercorrente", null, "GET",
							Utils.getApiUrl(system) + "/peticao/intercorrente/tipos", null,
							PeticaoIntercorrenteTiposGetResponse.class));
		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, SessionsCreatePost.TIMEOUT_MILLISECONDS);

		resp.list = new ArrayList<>();
		for (String system : mcr.responses.keySet()) {
			PeticaoIntercorrenteTiposGetResponse r = (PeticaoIntercorrenteTiposGetResponse) mcr.responses.get(system);
			if (r.list != null)
				for (TipoPeticaoIntercorrente i : r.list) {
					ConfigTipoPeticaoIntercorrente t = new ConfigTipoPeticaoIntercorrente();
					t.id = i.id;
					t.descricao = i.descricao;
					t.sistema = i.sistema;// .replace("SJ", "JF");
					resp.list.add(t);
				}
		}
	}

	@Override
	public String getContext() {
		return "obter tipos de petição intercorrente";
	}

}

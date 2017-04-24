package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPeticaoIntercorrenteTiposGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigPeticaoIntercorrenteTiposGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigTipoPeticaoIntercorrente;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigPeticaoIntercorrenteTiposGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.PeticaoIntercorrenteTiposGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.TipoPeticaoIntercorrente;

public class ConfigPeticaoIntercorrenteTiposGet implements IConfigPeticaoIntercorrenteTiposGet {
	private static final Logger log = LoggerFactory.getLogger(ConfigPeticaoIntercorrenteTiposGet.class);

	@Override
	public void run(ConfigPeticaoIntercorrenteTiposGetRequest req, ConfigPeticaoIntercorrenteTiposGetResponse resp)
			throws Exception {
		Future<SwaggerAsyncResponse<PeticaoIntercorrenteTiposGetResponse>> future = SwaggerCall.callAsync(
				"obter tipos de petição intercorrente", null, "GET",
				Utils.getWsProcessualUrl() + "/peticao/intercorrente/tipos", null,
				PeticaoIntercorrenteTiposGetResponse.class);
		SwaggerAsyncResponse<PeticaoIntercorrenteTiposGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		PeticaoIntercorrenteTiposGetResponse r = (PeticaoIntercorrenteTiposGetResponse) sar.getResp();

		resp.list = new ArrayList<>();
		if (r.list != null)
			for (TipoPeticaoIntercorrente i : r.list) {
				ConfigTipoPeticaoIntercorrente t = new ConfigTipoPeticaoIntercorrente();
				t.id = i.id;
				t.descricao = i.descricao;
				t.orgao = i.orgao;//.replace("SJ", "JF");
				resp.list.add(t);
			}
	}

	@Override
	public String getContext() {
		return "obter tipos de petição intercorrente";
	}

}

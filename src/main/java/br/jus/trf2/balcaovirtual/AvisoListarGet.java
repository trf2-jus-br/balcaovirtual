package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoListarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoListarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoListarGetResponse;

public class AvisoListarGet implements IAvisoListarGet {
	private static final Logger log = LoggerFactory.getLogger(AvisoListarGet.class);

	@Override
	public void run(AvisoListarGetRequest req, AvisoListarGetResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		SoapMNI.consultarAvisosPendentes(authorization, resp.list, resp.status);
	}

	@Override
	public String getContext() {
		return "consultar avisos pendentes";
	}

}

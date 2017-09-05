package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoListarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoListarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoListarGet;

public class AvisoListarGet implements IAvisoListarGet {

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

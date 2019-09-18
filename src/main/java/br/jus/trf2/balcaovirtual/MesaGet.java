package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.assijus.system.api.IAssijusSystem.DocListGetRequest;
import br.jus.trf2.assijus.system.api.IAssijusSystem.DocListGetResponse;
import br.jus.trf2.assijus.system.api.IAssijusSystem.Document;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Mesa;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class MesaGet implements IMesaGet {

	@Override
	public void run(MesaGetRequest req, MesaGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		resp.list = new ArrayList<>();
		Mesa m = new Mesa();
		m.id = "assijus";
		m.nome = "Assijus";
		resp.list.add(m);
	}

	@Override
	public String getContext() {
		return "listar mesas";
	}

}

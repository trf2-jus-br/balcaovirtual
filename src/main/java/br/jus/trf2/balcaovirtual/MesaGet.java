package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Mesa;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaGetResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;

public class MesaGet implements IMesaGet {

	@Override
	public void run(MesaGetRequest req, MesaGetResponse resp) throws Exception {
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

package br.jus.trf2.balcaojus;

import java.util.ArrayList;

import br.jus.trf2.balcaojus.IBalcaojus.IMesaGet;
import br.jus.trf2.balcaojus.IBalcaojus.Mesa;

public class MesaGet implements IMesaGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
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

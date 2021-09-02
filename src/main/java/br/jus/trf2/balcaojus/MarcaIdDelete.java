package br.jus.trf2.balcaojus;

import br.jus.trf2.balcaojus.IBalcaojus.IMarcaIdDelete;
import br.jus.trf2.balcaojus.model.Marca;

public class MarcaIdDelete implements IMarcaIdDelete {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		try (Dao dao = new Dao()) {
			Marca marca = dao.find(Marca.class, Long.valueOf(req.id));
			if (marca == null)
				return;
			dao.beginTransaction();
			dao.remove(marca);
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}

	}

	@Override
	public String getContext() {
		return "remover marca";
	}

}

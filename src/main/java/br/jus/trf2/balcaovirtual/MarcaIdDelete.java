package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMarcaIdDelete;
import br.jus.trf2.balcaovirtual.model.Marca;

public class MarcaIdDelete implements IMarcaIdDelete {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
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

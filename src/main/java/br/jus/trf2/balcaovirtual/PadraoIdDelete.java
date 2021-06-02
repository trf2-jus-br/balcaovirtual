package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPadraoIdDelete;
import br.jus.trf2.balcaovirtual.model.Padrao;

public class PadraoIdDelete implements IPadraoIdDelete {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		try (Dao dao = new Dao()) {
			Padrao padrao = dao.find(Padrao.class, Long.valueOf(req.id));
			if (padrao == null)
				return;
			dao.beginTransaction();
			dao.remove(padrao);
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "remover padrão";
	}

}

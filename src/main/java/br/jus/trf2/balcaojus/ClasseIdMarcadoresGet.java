package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.List;

import br.jus.trf2.balcaojus.IBalcaojus.IClasseIdMarcadoresGet;
import br.jus.trf2.balcaojus.IBalcaojus.Marcador;
import br.jus.trf2.balcaojus.model.TipoMarcaItem;

public class ClasseIdMarcadoresGet implements IClasseIdMarcadoresGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		resp.list = new ArrayList<>();

		try (Dao dao = new Dao()) {
			List<TipoMarcaItem> l = dao.obtemTipoMarcaItens(Long.valueOf(req.id));

			if (l == null)
				return;

			for (TipoMarcaItem timi : l) {
				Marcador m = new Marcador();
				m.texto = timi.getTimiNm();
				resp.list.add(m);
			}
		}

	}

	@Override
	public String getContext() {
		return "consultar marcadores";
	}

}

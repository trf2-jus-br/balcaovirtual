package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ClasseIdMarcadoresGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ClasseIdMarcadoresGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IClasseIdMarcadoresGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marcador;
import br.jus.trf2.balcaovirtual.model.TipoMarcaItem;

public class ClasseIdMarcadoresGet implements IClasseIdMarcadoresGet {

	@Override
	public void run(ClasseIdMarcadoresGetRequest req, ClasseIdMarcadoresGetResponse resp) throws Exception {
		resp.list = new ArrayList<>();

		if (!Utils.getMarcasAtivas())
			throw new PresentableUnloggedException("disabled");

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

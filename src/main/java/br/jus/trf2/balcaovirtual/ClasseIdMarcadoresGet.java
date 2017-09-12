package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

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

		// Usuario u = SessionsCreatePost.assertUsuario();
		// if (!"int".equals(u.origem))
		// return;

		if (true) {
			EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
			try {
				List<TipoMarcaItem> l = em.createQuery(Utils.getSQL("jpa-marcadores"))
						.setParameter("classe", Integer.valueOf(req.id)).getResultList();
				for (TipoMarcaItem i : l) {
					Marcador m = new Marcador();
					m.texto = i.getTimiNm();
					resp.list.add(m);
				}
			} finally {
				em.close();
			}
		} else {
			// Get documents from Oracle
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			try {
				conn = Utils.getConnection();
				pstmt = conn.prepareStatement(Utils.getSQL("marcadores"));
				pstmt.setString(1, req.id);
				rset = pstmt.executeQuery();

				while (rset.next()) {
					Marcador m = new Marcador();
					m.texto = rset.getString("TIMI_NM");
					resp.list.add(m);
				}
			} finally {
				if (rset != null)
					rset.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			}
		}
	}

	@Override
	public String getContext() {
		return "consultar marcadores";
	}

}

package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ClasseIdMarcadoresGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ClasseIdMarcadoresGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IClasseIdMarcadoresGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marcador;

public class ClasseIdMarcadoresGet implements IClasseIdMarcadoresGet {
	private static final Logger log = LoggerFactory.getLogger(ClasseIdMarcadoresGet.class);

	@Override
	public void run(ClasseIdMarcadoresGetRequest req, ClasseIdMarcadoresGetResponse resp) throws Exception {
		resp.list = new ArrayList<>();

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

	@Override
	public String getContext() {
		return "consultar marcadores";
	}

}

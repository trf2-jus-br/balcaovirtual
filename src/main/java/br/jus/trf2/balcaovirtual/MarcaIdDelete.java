package br.jus.trf2.balcaovirtual;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMarcaIdDelete;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MarcaIdDeleteRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MarcaIdDeleteResponse;

public class MarcaIdDelete implements IMarcaIdDelete {
	private static final Logger log = LoggerFactory.getLogger(MarcaIdDelete.class);

	@Override
	public void run(MarcaIdDeleteRequest req, MarcaIdDeleteResponse resp) throws Exception {
		Map<String, Object> map = SessionsCreatePost.assertUsuarioAutorizado();

		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = Utils.getConnection();
			cstmt = conn.prepareCall(Utils.getSQL("marca_remover"));
			cstmt.setString(1, req.id);
			cstmt.execute();
			resp.status = "OK";
		} finally {
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
	}

	@Override
	public String getContext() {
		return "remover marca";
	}

}

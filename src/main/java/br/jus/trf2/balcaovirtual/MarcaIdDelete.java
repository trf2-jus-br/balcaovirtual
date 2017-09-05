package br.jus.trf2.balcaovirtual;

import java.sql.CallableStatement;
import java.sql.Connection;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMarcaIdDelete;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MarcaIdDeleteRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MarcaIdDeleteResponse;

public class MarcaIdDelete implements IMarcaIdDelete {

	@Override
	public void run(MarcaIdDeleteRequest req, MarcaIdDeleteResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

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

package br.jus.trf2.balcaovirtual;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroSinalizarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Processo;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroSinalizarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroSinalizarPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ProcessoNumeroSinalizarPost implements IProcessoNumeroSinalizarPost {

	@Override
	public void run(ProcessoNumeroSinalizarPostRequest req, ProcessoNumeroSinalizarPostResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();

		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = Utils.getConnection();

			cstmt = conn.prepareCall("{ call sp_gravar_sinal(?,?,?,?,?,?,?,?) }");

			cstmt.setString(1, req.numero);
			if (req.favorito != null)
				cstmt.setBoolean(2, req.favorito);
			else
				cstmt.setString(2, null);
			if (req.recente != null)
				cstmt.setBoolean(3, req.recente);
			else
				cstmt.setString(3, null);
			cstmt.setBoolean(4, u.isInterno());
			cstmt.setString(5, u.usuario);

			// favorito
			cstmt.registerOutParameter(6, Types.BOOLEAN);

			// recente
			cstmt.registerOutParameter(7, Types.TIMESTAMP);

			// Error
			cstmt.registerOutParameter(8, Types.VARCHAR);

			cstmt.execute();

			if (cstmt.getString(8) != null)
				throw new PresentableException(cstmt.getString(8));

			// Produce response
			Processo p = new Processo();
			p.favorito = cstmt.getBoolean(6);
			p.recente = cstmt.getTimestamp(7);
			p.numero = req.numero;
			resp.processo = p;
		} finally {
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
	}

	@Override
	public String getContext() {
		return "gravar sinal";
	}

}

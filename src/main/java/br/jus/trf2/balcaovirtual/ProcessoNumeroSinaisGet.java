package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroSinaisGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Processo;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroSinaisGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroSinaisGetResponse;

public class ProcessoNumeroSinaisGet implements IProcessoNumeroSinaisGet {

	@Override
	public void run(ProcessoNumeroSinaisGetRequest req, ProcessoNumeroSinaisGetResponse resp) throws Exception {
		br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario u = SessionsCreatePost.assertUsuario();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = Utils.getConnection();
			pstmt = conn.prepareStatement(Utils.getSQL("sinais"));
			pstmt.setString(1, req.numero);
			pstmt.setString(2, u.usuario);
			pstmt.setBoolean(3, u.isInterno());
			rset = pstmt.executeQuery();

			resp.processo = new Processo();
			resp.processo.numero = req.numero;

			while (rset.next()) {
				resp.processo.favorito = rset.getBoolean("sina_lg_favorito");
				resp.processo.recente = rset.getTimestamp("sina_df_recente");
				break;
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
		return "obter sinais";
	}

}

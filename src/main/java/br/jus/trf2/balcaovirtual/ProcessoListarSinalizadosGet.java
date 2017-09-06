package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoListarSinalizadosGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Processo;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoListarSinalizadosGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoListarSinalizadosGetResponse;

public class ProcessoListarSinalizadosGet implements IProcessoListarSinalizadosGet {

	@Override
	public void run(ProcessoListarSinalizadosGetRequest req, ProcessoListarSinalizadosGetResponse resp)
			throws Exception {
		br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario u = SessionsCreatePost.assertUsuario();
		resp.list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = Utils.getConnection();
			pstmt = conn.prepareStatement(Utils.getSQL("sinalizados"));
			pstmt.setString(1, u.usuario);
			pstmt.setBoolean(2, u.isInterno());
			rset = pstmt.executeQuery();

			while (rset.next()) {
				Processo p = new Processo();
				p.numero = rset.getString("sina_cd_proc");
				p.favorito = rset.getBoolean("sina_lg_favorito");
				p.recente = rset.getTimestamp("sina_df_recente");
				resp.list.add(p);
			}
		} finally {
			if (rset != null)
				rset.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}

		// resp.list = new ArrayList<>();
		// Processo p = new Processo();
		// p.numero = "00002294020154020000";
		// p.favorito = "true";
		// resp.list.add(p);
	}

	@Override
	public String getContext() {
		return "obter processos sinalizados";
	}

}

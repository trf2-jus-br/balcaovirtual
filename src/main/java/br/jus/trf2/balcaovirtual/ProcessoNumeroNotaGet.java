package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroNotaGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marca;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Nota;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroNotaGet implements IProcessoNumeroNotaGet {
	private static class MarcaTabela extends Marca {
		public String numero;
		public String orgao;
	}

	private static final List<MarcaTabela> list = new ArrayList<>();

	@Override
	public void run(ProcessoNumeroNotaGetRequest req, ProcessoNumeroNotaGetResponse resp) throws Exception {
		if (!Utils.getMarcasAtivas())
			throw new PresentableUnloggedException("disabled");

		Usuario u = SessionsCreatePost.assertUsuario();
		if (u.usuarios == null)
			throw new PresentableException("Usuário não possui identificador e unidade");
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());
		if (ud == null)
			throw new PresentableUnloggedException("disabled");

		resp.list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = Utils.getConnection();
			pstmt = conn.prepareStatement(Utils.getSQL("notas"));
			pstmt.setString(1, req.numero);
			pstmt.setString(2, req.orgao);
			pstmt.setLong(3, ud.id);
			if (ud.unidade != null)
				pstmt.setLong(4, ud.unidade);
			else
				pstmt.setString(4, null);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				Nota m = new Nota();
				m.idnota = rset.getString("nota_id");
				m.texto = rset.getString("nota_tx_conteudo");
				m.pessoal = rset.getBoolean("nota_lg_pessoal");
				m.nomeusuario = rset.getString("nota_nm_usu");
				m.dataalteracao = rset.getTimestamp("nota_df_alteracao");
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
		return "consultar marcas";
	}

}

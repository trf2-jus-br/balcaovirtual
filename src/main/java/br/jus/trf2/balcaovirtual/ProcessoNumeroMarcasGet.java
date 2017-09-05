package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroMarcasGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marca;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroMarcasGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroMarcasGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroMarcasGet implements IProcessoNumeroMarcasGet {
	private static class MarcaTabela extends Marca {
		public String numero;
		public String orgao;
	}

	private static final List<MarcaTabela> list = new ArrayList<>();

	@Override
	public void run(ProcessoNumeroMarcasGetRequest req, ProcessoNumeroMarcasGetResponse resp) throws Exception {
		if (!Utils.getMarcasAtivas())
			throw new PresentableUnloggedException("disabled");

		Usuario u = SessionsCreatePost.assertUsuario();
		if (u.usuarios == null)
			throw new PresentableException("Usuário não possui identificador e unidade");
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());

		resp.list = new ArrayList<>();

		if (ud == null)
			throw new PresentableUnloggedException("disabled");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = Utils.getConnection();
			pstmt = conn.prepareStatement(Utils.getSQL("marcas"));
			pstmt.setString(1, req.numero);
			pstmt.setString(2, req.orgao);
			pstmt.setBoolean(3, u.isInterno());
			pstmt.setLong(4, ud.id);
			if (ud.unidade != null)
				pstmt.setLong(5, ud.unidade);
			else
				pstmt.setString(5, null);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				Marca m = new Marca();
				m.idmarca = rset.getString("marc_id");
				String complemento = rset.getString("marc_tx_conteudo");
				String marcador = rset.getString("timi_nm");
				m.texto = marcador != null ? marcador + (complemento != null ? " - " + complemento : "") : complemento;
				m.idestilo = rset.getString("esti_id");
				m.idpeca = rset.getString("marc_id_peca");
				m.paginicial = rset.getString("marc_nr_pag_inicial");
				m.pagfinal = rset.getString("marc_nr_pag_final");
				m.nomeusuario = rset.getString("marc_nm_usu");
				m.dataalteracao = rset.getTimestamp("marc_df_alteracao");
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

		for (MarcaTabela mt : list) {
			if (!req.numero.equals(mt.numero) || !req.orgao.equals(mt.orgao))
				continue;
		}
	}

	@Override
	public String getContext() {
		return "consultar marcas";
	}

}

package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroMarcasGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marca;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marcador;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroMarcasGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroMarcasGetResponse;

public class ProcessoNumeroMarcasGet implements IProcessoNumeroMarcasGet {
	private static class MarcaTabela extends Marca {
		public String numero;
		public String orgao;
	}

	private static final Logger log = LoggerFactory.getLogger(ProcessoNumeroMarcasGet.class);
	private static final List<MarcaTabela> list = new ArrayList<>();

	@Override
	public void run(ProcessoNumeroMarcasGetRequest req, ProcessoNumeroMarcasGetResponse resp) throws Exception {
		Map<String, Object> map = SessionsCreatePost.assertUsuarioAutorizado();
		resp.list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = Utils.getConnection();
			pstmt = conn.prepareStatement(Utils.getSQL("marcas"));
			pstmt.setString(1, req.numero);
			pstmt.setString(2, req.orgao);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				Marca m = new Marca();
				m.idmarca = rset.getString("marc_id");
				String complemento = rset.getString("marc_tx");
				String marcador = rset.getString("timi_nm");
				m.texto = marcador != null ? marcador + (complemento != null ? " - " + complemento : "") : complemento;
				m.cor = rset.getString("esti_tp_cor");
				m.idpeca = rset.getString("marc_id_peca");
				m.paginicial = rset.getString("marc_pag_inicial");
				m.pagfinal = rset.getString("marc_pag_final");
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

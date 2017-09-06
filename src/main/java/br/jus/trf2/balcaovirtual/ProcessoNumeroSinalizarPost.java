package br.jus.trf2.balcaovirtual;

import java.sql.CallableStatement;
import java.sql.Connection;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroSinalizarPost;
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

//			cstmt = conn.prepareCall("{ call sp_gravar_marca(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
//
//			cstmt.setString(1, req.numero);
//			cstmt.setString(2, req.orgao);
//			cstmt.setString(3, req.idclasse);
//			cstmt.setString(4, req.id);
//			cstmt.setString(5, req.idmarca);
//			cstmt.setString(6, req.texto);
//			cstmt.setString(7, req.idestilo);
//			cstmt.setString(8, req.paginicial);
//			cstmt.setString(9, req.pagfinal);
//			cstmt.setBoolean(10, u.isInterno());
//			cstmt.setString(11, u.nome);
//
//			cstmt.setLong(12, ud.id);
//			if (ud.unidade != null)
//				cstmt.setLong(13, ud.unidade);
//			else
//				cstmt.setString(13, null);
//
//			// nova idmarca
//			cstmt.registerOutParameter(14, Types.VARCHAR);
//
//			// timi_nm
//			cstmt.registerOutParameter(15, Types.VARCHAR);
//
//			// complemento
//			cstmt.registerOutParameter(16, Types.VARCHAR);
//
//			// Error
//			cstmt.registerOutParameter(17, Types.VARCHAR);
//
//			cstmt.execute();
//
//			if (cstmt.getString(17) != null)
//				throw new PresentableException(cstmt.getString(17));
//
//			// Produce response
//			Marca m = new Marca();
//			m.idmarca = cstmt.getString(14);
//			m.idpeca = req.id;
//
//			String complemento = cstmt.getString(16);
//			String marcador = cstmt.getString(15);
//			m.texto = marcador != null ? marcador + (complemento != null ? " - " + complemento : "") : complemento;
//			m.idestilo = req.idestilo;
//			m.paginicial = req.paginicial;
//			m.pagfinal = req.pagfinal;
//			resp.marca = m;
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

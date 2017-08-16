package br.jus.trf2.balcaovirtual;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPecaIdMarcaPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marca;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPecaIdMarcaPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPecaIdMarcaPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroPecaIdMarcaPost implements IProcessoNumeroPecaIdMarcaPost {
	private static final Logger log = LoggerFactory.getLogger(ProcessoNumeroPecaIdMarcaPost.class);

	@Override
	public void run(ProcessoNumeroPecaIdMarcaPostRequest req, ProcessoNumeroPecaIdMarcaPostResponse resp)
			throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());

		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = Utils.getConnection();

			cstmt = conn.prepareCall("{ call gravar_marca(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			cstmt.setString(1, req.numero);
			cstmt.setString(2, req.orgao);
			cstmt.setString(3, req.idclasse);
			cstmt.setString(4, req.id);
			cstmt.setString(5, req.idmarca);
			cstmt.setString(6, req.texto);
			cstmt.setString(7, req.idestilo);
			cstmt.setString(8, req.paginicial);
			cstmt.setString(9, req.pagfinal);
			cstmt.setBoolean(10, u.isInterno());
			cstmt.setLong(11, ud.id);
			if (ud.unidade != null)
				cstmt.setLong(12, ud.unidade);
			else
				cstmt.setString(12, null);

			// nova idmarca
			cstmt.registerOutParameter(13, Types.VARCHAR);

			// timi_nm
			cstmt.registerOutParameter(14, Types.VARCHAR);

			// complemento
			cstmt.registerOutParameter(15, Types.VARCHAR);

			// Error
			cstmt.registerOutParameter(16, Types.VARCHAR);

			cstmt.execute();

			if (cstmt.getString(16) != null)
				throw new PresentableException(cstmt.getString(16));

			// Produce response
			Marca m = new Marca();
			m.idmarca = cstmt.getString(13);
			m.idpeca = req.id;

			String complemento = cstmt.getString(15);
			String marcador = cstmt.getString(14);
			m.texto = marcador != null ? marcador + (complemento != null ? " - " + complemento : "") : complemento;
			m.idestilo = req.idestilo;
			m.paginicial = req.paginicial;
			m.pagfinal = req.pagfinal;
			resp.marca = m;
		} finally {
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
	}

	@Override
	public String getContext() {
		return "gravar marca";
	}

}

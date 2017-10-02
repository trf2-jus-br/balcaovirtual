package br.jus.trf2.balcaovirtual;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroNotaPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marca;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroNotaPost implements IProcessoNumeroNotaPost {

	@Override
	public void run(ProcessoNumeroNotaPostRequest req, ProcessoNumeroNotaPostResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());

		if (ud == null)
			throw new PresentableUnloggedException("Usuário '" + u.usuario
					+ "' não pode fazer anotações porque não foi autenticado no órgão '" + req.orgao + "'.");

		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = Utils.getConnection();

			cstmt = conn.prepareCall("{ call sp_gravar_marca(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			cstmt.setString(1, req.numero);
			cstmt.setString(2, req.orgao);
			cstmt.setString(3, null);
			cstmt.setString(4, req.texto);
			cstmt.setBoolean(5, req.pessoal);
			cstmt.setBoolean(6, u.isInterno());
			cstmt.setString(7, u.usuario);
			cstmt.setString(8, u.nome);

			cstmt.setLong(9, ud.id);
			if (ud.unidade != null)
				cstmt.setLong(10, ud.unidade);
			else
				cstmt.setString(10, null);

			// nova idmarca
			cstmt.registerOutParameter(15, Types.VARCHAR);

			// timi_nm
			cstmt.registerOutParameter(16, Types.VARCHAR);

			// complemento
			cstmt.registerOutParameter(17, Types.VARCHAR);

			// Error
			cstmt.registerOutParameter(18, Types.VARCHAR);

			cstmt.execute();

			if (cstmt.getString(18) != null)
				throw new PresentableException(cstmt.getString(18));

			// Produce response
			Marca m = new Marca();
			m.idmarca = cstmt.getString(15);
			m.idpeca = req.id;

			String complemento = cstmt.getString(17);
			String marcador = cstmt.getString(16);
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

package br.jus.trf2.balcaovirtual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroMarcasGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Marca;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroMarcasGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroMarcasGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroMarcasGet implements IProcessoNumeroMarcasGet {
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

		if (true) {
			EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
			try {
				List<Object[]> l = (List<Object[]>) em.createQuery(Utils.getSQL("jpa-marcas"))
						.setParameter("processo", req.numero).setParameter("orgao", req.orgao)
						.setParameter("interno", u.isInterno()).setParameter("usuario", ud.id.longValue())
						.setParameter("unidade", ud.unidade != null ? ud.unidade.longValue() : null).getResultList();

				for (Object[] i : l) {
					br.jus.trf2.balcaovirtual.model.Marca m = (br.jus.trf2.balcaovirtual.model.Marca) i[0];
					String t = (String) i[1];

					Marca r = new Marca();
					r.dataalteracao = m.getMarcDfAlteracao();
					r.idestilo = Long.toString(m.getEstilo().getEstiId());
					r.idmarca = Long.toString(m.getMarcId());
					r.idpeca = Long.toString(m.getMarcIdPeca());
					r.nomeusuario = m.getMarcNmUsu();
					r.paginicial = m.getMarcNrPagInicial() != null ? m.getMarcNrPagInicial().toString() : null;
					r.pagfinal = m.getMarcNrPagFinal() != null ? m.getMarcNrPagFinal().toString() : null;
					r.texto = m.getMarcTxConteudo();
					r.texto = t != null ? t + (m.getMarcTxConteudo() != null ? " - " + m.getMarcTxConteudo() : "")
							: m.getMarcTxConteudo();
					resp.list.add(r);
				}
			} finally {
				em.close();
			}
		} else {
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
					m.texto = marcador != null ? marcador + (complemento != null ? " - " + complemento : "")
							: complemento;
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
		}
	}

	@Override
	public String getContext() {
		return "consultar marcas";
	}

}

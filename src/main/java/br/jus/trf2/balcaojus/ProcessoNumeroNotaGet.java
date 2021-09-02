package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroNotaGet;
import br.jus.trf2.balcaojus.model.Nota;
import br.jus.trf2.balcaojus.model.Processo;

public class ProcessoNumeroNotaGet implements IProcessoNumeroNotaGet {
	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.usuarios == null)
			throw new PresentableException("Usuário não possui identificador e unidade");
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());
		if (ud == null)
			throw new PresentableUnloggedException("disabled");

		resp.list = new ArrayList<>();

		try (Dao dao = new Dao()) {
			Processo p = dao.obtemProcesso(req.numero, req.sistema, false);
			if (p == null)
				return;

			List<Nota> l = dao.obtemNotas(p, ud.id, ud.codunidade);
			if (l == null)
				return;

			for (Nota nota : l) {
				br.jus.trf2.balcaojus.IBalcaojus.Nota m = new br.jus.trf2.balcaojus.IBalcaojus.Nota();
				m.idnota = Long.toString(nota.getNotaId());
				m.texto = nota.getNotaTxConteudo();
				m.pessoal = nota.getNotaLgPessoal();
				m.nomeusuario = nota.getNotaNmUsu();
				m.dataalteracao = nota.getNotaDfAlteracao();
				resp.list.add(m);
			}
		}
	}

	@Override
	public String getContext() {
		return "consultar marcas";
	}

}

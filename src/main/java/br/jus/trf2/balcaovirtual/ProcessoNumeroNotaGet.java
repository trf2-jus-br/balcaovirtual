package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroNotaGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroNotaGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;
import br.jus.trf2.balcaovirtual.model.Nota;
import br.jus.trf2.balcaovirtual.model.Processo;

public class ProcessoNumeroNotaGet implements IProcessoNumeroNotaGet {
	@Override
	public void run(ProcessoNumeroNotaGetRequest req, ProcessoNumeroNotaGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		if (u.usuarios == null)
			throw new PresentableException("Usuário não possui identificador e unidade");
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());
		if (ud == null)
			throw new PresentableUnloggedException("disabled");

		resp.list = new ArrayList<>();

		try (Dao dao = new Dao()) {
			Processo p = dao.obtemProcesso(req.numero, req.orgao, false);
			if (p == null)
				return;

			List<Nota> l = dao.obtemNotas(p, ud.id, ud.unidade);
			if (l == null)
				return;

			for (Nota nota : l) {
				br.jus.trf2.balcaovirtual.IBalcaoVirtual.Nota m = new br.jus.trf2.balcaovirtual.IBalcaoVirtual.Nota();
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

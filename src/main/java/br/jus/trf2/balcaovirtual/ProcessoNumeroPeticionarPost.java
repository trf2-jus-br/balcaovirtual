package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPeticionarPost;

public class ProcessoNumeroPeticionarPost implements IProcessoNumeroPeticionarPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(u.usuario, u.getSenha(), req.sistema, req.numero,
				req.tipopeticao, Integer.parseInt(req.nivelsigilo), req.encerraprazos, req.observacoes, req.pdfs, null);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPeticionarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticionarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticionarPostResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;

public class ProcessoNumeroPeticionarPost implements IProcessoNumeroPeticionarPost {

	@Override
	public void run(ProcessoNumeroPeticionarPostRequest req, ProcessoNumeroPeticionarPostResponse resp)
			throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(u.usuario, u.getSenha(), req.sistema, req.numero,
				req.tipopeticao, Integer.parseInt(req.nivelsigilo), req.encerraprazos, req.pdfs, null);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

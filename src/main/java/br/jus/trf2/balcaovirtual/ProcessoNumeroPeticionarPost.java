package br.jus.trf2.balcaovirtual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPeticionarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticionarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticionarPostResponse;

public class ProcessoNumeroPeticionarPost implements IProcessoNumeroPeticionarPost {
	private static final Logger log = LoggerFactory.getLogger(ProcessoNumeroValidarGet.class);

	@Override
	public void run(ProcessoNumeroPeticionarPostRequest req, ProcessoNumeroPeticionarPostResponse resp)
			throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(authorization, req.orgao, req.numero,
				req.tipopeticao.split("-")[1], Integer.parseInt(req.nivelsigilo), req.pdfs);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

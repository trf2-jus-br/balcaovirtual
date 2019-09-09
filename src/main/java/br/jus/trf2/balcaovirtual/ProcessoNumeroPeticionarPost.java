package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPeticionarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticionarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPeticionarPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class ProcessoNumeroPeticionarPost implements IProcessoNumeroPeticionarPost {

	@Override
	public void run(ProcessoNumeroPeticionarPostRequest req, ProcessoNumeroPeticionarPostResponse resp)
			throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();

		if (req.encerraprazos && u.cpf == null)
			throw new PresentableException(
					"CPF não pode ser nulo quando o usuário deseja encerrar prazos no peticionamento intercorrente");

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(u.usuario, u.senha, req.sistema, req.numero,
				req.tipopeticao.split("-")[1], Integer.parseInt(req.nivelsigilo), req.encerraprazos ? u.cpf : null,
				req.pdfs, null);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroCotaEnviarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaEnviarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaEnviarPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroCotaEnviarPost implements IProcessoNumeroCotaEnviarPost {

	@Override
	public void run(ProcessoNumeroCotaEnviarPostRequest req, ProcessoNumeroCotaEnviarPostResponse resp)
			throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());
		byte[] pdf = ProcessoNumeroCotaPrevisaoPdfPost.criarPDF(u.nome, req.numero, req.texto, req.cargo, req.empresa,
				req.unidade);

		String tipo = SwaggerServlet.getProperty(req.sistema.toLowerCase() + ".cota.tipo");

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(u.usuario, u.senha, req.sistema, req.numero, tipo,
				Integer.parseInt(req.nivelsigilo), null, null, pdf);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar cota";
	}

}

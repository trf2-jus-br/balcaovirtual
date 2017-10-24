package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroCotaEnviarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaEnviarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaEnviarPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;

public class ProcessoNumeroCotaEnviarPost implements IProcessoNumeroCotaEnviarPost {

	@Override
	public void run(ProcessoNumeroCotaEnviarPostRequest req, ProcessoNumeroCotaEnviarPostResponse resp)
			throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());
		byte[] pdf = ProcessoNumeroCotaPrevisaoPdfPost.criarPDF(u.nome, req.numero, req.texto);

		String tipo = SwaggerUtils.getRequiredProperty("balcaovirtual." + req.orgao.toLowerCase() + ".cota.tipo",
				"Parâmetro de tipo de documento não configurado", true);

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(authorization, req.orgao, req.numero, tipo,
				Integer.parseInt(req.nivelsigilo), null, pdf);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar cota";
	}

}

package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroCotaEnviarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaEnviarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaEnviarPostResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.AutenticarPost.UsuarioDetalhe;

public class ProcessoNumeroCotaEnviarPost implements IProcessoNumeroCotaEnviarPost {

	@Override
	public void run(ProcessoNumeroCotaEnviarPostRequest req, ProcessoNumeroCotaEnviarPostResponse resp)
			throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());
		byte[] pdf = ProcessoNumeroCotaPrevisaoPdfPost.criarPDF(u.nome, req.numero, req.texto, req.cargo, req.empresa,
				req.unidade);

		String tipo = BalcaoVirtualServlet.INSTANCE.getProperty(req.sistema.toLowerCase() + ".cota.tipo");

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(u.usuario, u.getSenha(), req.sistema, req.numero, tipo,
				Integer.parseInt(req.nivelsigilo), null, null, null, pdf);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar cota";
	}

}

package br.jus.trf2.balcaojus;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroCotaEnviarPost;

public class ProcessoNumeroCotaEnviarPost implements IProcessoNumeroCotaEnviarPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());
		byte[] pdf = ProcessoNumeroCotaPrevisaoPdfPost.criarPDF(u.nome, req.numero, req.texto, req.cargo, req.empresa,
				req.unidade);

		String tipo = BalcaojusServlet.INSTANCE.getProperty(req.sistema.toLowerCase() + ".cota.tipo");

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(u.usuario, u.getSenha(), req.sistema, req.numero, tipo,
				Integer.parseInt(req.nivelsigilo), null, null, null, pdf);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar cota";
	}

}

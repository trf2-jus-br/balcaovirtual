package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ISugestaoPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SugestaoPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SugestaoPostResponse;

public class SugestaoPost implements ISugestaoPost {

	@Override
	public void run(SugestaoPostRequest req, SugestaoPostResponse resp) throws Exception {
		AutenticarPost.assertAuthorization();
		StringBuilder sb = new StringBuilder();
		sb.append("Nome: ");
		sb.append(req.nome);
		sb.append("\nEmail: ");
		sb.append(req.email);
		sb.append("\n\nMensagem: ");
		sb.append(req.mensagem);
		Correio.enviar(SwaggerServlet.getProperty("smtp.destinatario"), SwaggerServlet.getProperty("smtp.assunto"),
				sb.toString(), null, null, null);
	}

	@Override
	public String getContext() {
		return "enviar sugestao";
	}

}

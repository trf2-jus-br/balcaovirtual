package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ISugestaoPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SugestaoPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.SugestaoPostResponse;

public class SugestaoPost implements ISugestaoPost {

	@Override
	public void run(SugestaoPostRequest req, SugestaoPostResponse resp) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome: ");
		sb.append(req.nome);
		sb.append("\nEmail: ");
		sb.append(req.email);
		sb.append("\n\nMensagem: ");
		sb.append(req.mensagem);
		Correio.enviar(BalcaoVirtualServlet.INSTANCE.getProperty("smtp.destinatario"), BalcaoVirtualServlet.INSTANCE.getProperty("smtp.assunto"),
				sb.toString(), null, null, null);
	}

	@Override
	public String getContext() {
		return "enviar sugestao";
	}

}

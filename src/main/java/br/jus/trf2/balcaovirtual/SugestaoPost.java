package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.SwaggerUtils;

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
		Correio.enviar(
				SwaggerUtils.getRequiredProperty("balcaovirtual.smtp.destinatario",
						"Não foi especificado o destinatário do email de sugestões.", false),
				SwaggerUtils.getProperty("balcaovirtual.smtp.assunto", "Balcão Virtual: Sugestão"), sb.toString(), null,
				null, null);
	}

	@Override
	public String getContext() {
		return "enviar sugestao";
	}

}

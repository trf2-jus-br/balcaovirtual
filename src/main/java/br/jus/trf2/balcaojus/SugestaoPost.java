package br.jus.trf2.balcaojus;

import br.jus.trf2.balcaojus.IBalcaojus.ISugestaoPost;

public class SugestaoPost implements ISugestaoPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome: ");
		sb.append(req.nome);
		sb.append("\nEmail: ");
		sb.append(req.email);
		sb.append("\n\nMensagem: ");
		sb.append(req.mensagem);
		Correio.enviar(BalcaojusServlet.INSTANCE.getProperty("smtp.destinatario"),
				BalcaojusServlet.INSTANCE.getProperty("smtp.assunto"), sb.toString(), null, null, null);
	}

	@Override
	public String getContext() {
		return "enviar sugestao";
	}

}

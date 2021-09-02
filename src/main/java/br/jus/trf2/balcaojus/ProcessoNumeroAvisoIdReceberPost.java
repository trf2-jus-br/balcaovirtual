package br.jus.trf2.balcaojus;

import org.joda.time.LocalDateTime;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroAvisoIdReceberPost;

public class ProcessoNumeroAvisoIdReceberPost implements IProcessoNumeroAvisoIdReceberPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		SoapMNI.consultarTeorComunicacao(u.usuario, u.getSenha(), req.numero, req.id, req.sistema, resp);
		resp.datarecebimento = LocalDateTime.now().toString("dd/MM/yyyy hh:mm");
	}

	@Override
	public String getContext() {
		return "receber aviso";
	}

}

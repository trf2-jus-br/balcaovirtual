package br.jus.trf2.balcaovirtual;

import org.joda.time.LocalDateTime;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroAvisoIdReceberPost;

public class ProcessoNumeroAvisoIdReceberPost implements IProcessoNumeroAvisoIdReceberPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		SoapMNI.consultarTeorComunicacao(u.usuario, u.getSenha(), req.numero, req.id, req.sistema, resp);
		resp.datarecebimento = LocalDateTime.now().toString("dd/MM/yyyy hh:mm");
	}

	@Override
	public String getContext() {
		return "receber aviso";
	}

}

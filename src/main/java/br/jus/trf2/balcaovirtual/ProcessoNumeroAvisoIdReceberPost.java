package br.jus.trf2.balcaovirtual;

import org.joda.time.LocalDateTime;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroAvisoIdReceberPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroAvisoIdReceberPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroAvisoIdReceberPostResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;

public class ProcessoNumeroAvisoIdReceberPost implements IProcessoNumeroAvisoIdReceberPost {

	@Override
	public void run(ProcessoNumeroAvisoIdReceberPostRequest req, ProcessoNumeroAvisoIdReceberPostResponse resp)
			throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

		SoapMNI.consultarTeorComunicacao(u.usuario, u.senha, req.numero, req.id, req.sistema, resp);
		resp.datarecebimento = LocalDateTime.now().toString("dd/MM/yyyy hh:mm");
	}

	@Override
	public String getContext() {
		return "receber aviso";
	}

}

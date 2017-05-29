package br.jus.trf2.balcaovirtual;

import java.util.Map;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroAvisoIdReceberPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroAvisoIdReceberPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroAvisoIdReceberPostResponse;

public class ProcessoNumeroAvisoIdReceberPost implements IProcessoNumeroAvisoIdReceberPost {
	private static final Logger log = LoggerFactory.getLogger(ProcessoNumeroAvisoIdReceberPost.class);

	@Override
	public void run(ProcessoNumeroAvisoIdReceberPostRequest req, ProcessoNumeroAvisoIdReceberPostResponse resp)
			throws Exception {
		String authorization = BalcaoVirtualServlet.getHttpServletRequest().getHeader("Authorization");
		if (authorization.startsWith("Bearer "))
			authorization = authorization.substring(7);
		Map<String, Object> jwt = SessionsCreatePost.verify(authorization);

		SoapMNI.consultarTeorComunicacao(authorization, req.numero, req.id, req.orgao, resp);
		resp.datarecebimento = LocalDateTime.now().toString("dd/MM/yyyy hh:mm");
	}

	@Override
	public String getContext() {
		return "receber aviso";
	}

}

package br.jus.trf2.balcaovirtual;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroPdfGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroPdfGetResponse;

public class ProcessoNumeroPdfGet implements IProcessoNumeroPdfGet {
	private static final Logger log = LoggerFactory.getLogger(ProcessoNumeroPdfGet.class);

	@Override
	public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception {
		Map<String, Object> map = SessionsCreatePost.assertUsuarioAutorizado();
		if (!"giovanni.souza".equals(map.get("username")))
			throw new PresentableException("Operação temporariamente desabilitada.");
		resp.jwt = DownloadJwtFilenameGet.jwt((String) map.get("username"), req.orgao, req.numero, null);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

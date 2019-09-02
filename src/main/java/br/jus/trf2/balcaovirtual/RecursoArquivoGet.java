package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IRecursoArquivoGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.RecursoArquivoGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.RecursoArquivoGetResponse;

public class RecursoArquivoGet implements IRecursoArquivoGet {

	@Override
	public void run(RecursoArquivoGetRequest req, RecursoArquivoGetResponse resp) throws Exception {
		switch (req.arquivo) {
		case "mniwsdl.xml":
		case "mniwsdl0.xml":
		case "mnixsd0.xml":
		case "mnixsd1.xml":
		case "mnixsd2.xml":
			break;
		default:
			throw new Exception("Recurso inválido");
		}
		InputStream is = this.getClass().getResourceAsStream(req.arquivo);
		String sSwagger = SwaggerUtils.convertStreamToString(is);
		sSwagger = sSwagger.replace("http://localhost:8080/balcaovirtual", SwaggerServlet.getProperty("base.url"));
		byte[] ab = sSwagger.getBytes();
		resp.contentlength = (long) ab.length;
		resp.contentdisposition = "inline;filename=" + req.arquivo;
		resp.inputstream = new ByteArrayInputStream(ab);
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

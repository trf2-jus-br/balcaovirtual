package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ArquivoTemporarioPdfGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ArquivoTemporarioPdfGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IArquivoTemporarioPdfGet;

public class ArquivoTemporarioPdfGet implements IArquivoTemporarioPdfGet {
	private static final Logger log = LoggerFactory.getLogger(ArquivoTemporarioPdfGet.class);

	@Override
	public void run(ArquivoTemporarioPdfGetRequest req, ArquivoTemporarioPdfGetResponse resp) throws Exception {
		byte[] ab = Files.readAllBytes(Paths.get(Utils.getDirFinal() + "/" + req.pdf + ".pdf"));
		resp.contentlength = (long) ab.length;
		resp.contentdisposition = "inline;filename=" + req.pdf + ".pdf";
		resp.inputstream = new ByteArrayInputStream(ab);
	}

	@Override
	public String getContext() {
		return "obter arquivo temporário";
	}

}

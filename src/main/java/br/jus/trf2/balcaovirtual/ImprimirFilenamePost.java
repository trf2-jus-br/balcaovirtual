package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IImprimirFilenamePost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ImprimirFilenamePostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ImprimirFilenamePostResponse;

public class ImprimirFilenamePost implements IImprimirFilenamePost {

	@Override
	public void run(ImprimirFilenamePostRequest req, ImprimirFilenamePostResponse resp) throws Exception {
		byte[] ab = new Html2Pdf().converterJsoup(req.html, false);

		resp.contentdisposition = req.disposition + ";filename=" + req.filename;
		resp.contentlength = (long) ab.length;
		resp.contenttype = "application/pdf";
		resp.inputstream = new ByteArrayInputStream(ab);
	}

	@Override
	public String getContext() {
		return "gerar PDF";
	}

}

package br.jus.trf2.balcaojus;

import java.io.ByteArrayInputStream;

import br.jus.trf2.balcaojus.IBalcaojus.IImprimirFilenamePost;
import br.jus.trf2.balcaojus.util.AcessoPublico;

@AcessoPublico
public class ImprimirFilenamePost implements IImprimirFilenamePost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		byte[] ab = new Html2Pdf().converterJsoup(req.html, false);

		resp.contentdisposition = req.disposition + ";filename=" + req.filename;
		resp.contentlength = (long) ab.length;
		resp.contenttype = "application/pdf";
		resp.inputstream = new ByteArrayInputStream(ab);

		ImprimirFilenameGet.armazenarPdfEmCache(req.filename, null, req.html, req.disposition);
	}

	@Override
	public String getContext() {
		return "gerar PDF";
	}

}

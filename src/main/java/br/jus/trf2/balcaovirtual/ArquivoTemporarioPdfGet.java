package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IArquivoTemporarioPdfGet;

public class ArquivoTemporarioPdfGet implements IArquivoTemporarioPdfGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
//		SessionsCreatePost.assertAuthorization();
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

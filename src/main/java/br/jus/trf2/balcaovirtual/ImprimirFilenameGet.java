package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IImprimirFilenameGet;
import br.jus.trf2.balcaovirtual.util.AcessoPublico;

@AcessoPublico
public class ImprimirFilenameGet implements IImprimirFilenameGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		CachedHtml ch = recuperarPdfEmCache(req.filename);
		if (ch.pdf == null && ch.html != null)
			ch.pdf = new Html2Pdf().converterJsoup(ch.html, false);
		resp.contentdisposition = ch.disposition + ";filename=" + req.filename;
		resp.contentlength = (long) ch.pdf.length;
		resp.contenttype = "application/pdf";
		resp.inputstream = new ByteArrayInputStream(ch.pdf);
	}

	@Override
	public String getContext() {
		return "gerar PDF";
	}

	public static void armazenarPdfEmCache(String filename, byte[] pdf, String html, String disposition)
			throws Exception {
		CachedHtml ch = new CachedHtml();
		ch.pdf = pdf;
		ch.html = html;
		ch.disposition = disposition;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(ch);
		oos.flush();
		byte[] data = bos.toByteArray();

		SwaggerUtils.memCacheStore(filename, data);
	}

	public static CachedHtml recuperarPdfEmCache(String filename) throws Exception {
		byte[] data = SwaggerUtils.memCacheRetrieve(filename);
		if (data == null)
			throw new PresentableException("Conteúdo da certidão não encontrado no cache. Por favor, emita novamente.");
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bis);
		CachedHtml ch = (CachedHtml) ois.readObject();
		return ch;
	}

	@SuppressWarnings("serial")
	public static class CachedHtml implements Serializable {
		public byte[] pdf;
		public String html;
		public String disposition;
	}

}

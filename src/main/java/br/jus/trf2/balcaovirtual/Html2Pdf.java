package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class Html2Pdf {
	private static Logger logger = Logger.getLogger(Html2Pdf.class.getCanonicalName());

	private String cleanHtml(String data) throws UnsupportedEncodingException {
		System.err.println("transformando HTML em XHTML");
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setInputEncoding("UTF-8");
		tidy.setOutputEncoding("UTF-8");
		// tidy.setWraplen(Integer.MAX_VALUE);
		// tidy.setPrintBodyOnly(true);
		// tidy.setXmlOut(true);
		tidy.setSmartIndent(true);
		tidy.setTidyMark(false);
		tidy.setShowErrors(0);
		tidy.setQuiet(true);
		// tidy.setErrout(null);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		tidy.parseDOM(inputStream, outputStream);
		System.err.println("retornando XHTML");
		String s = outputStream.toString("UTF-8");
		s = s.replace(
				" PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"",
				"");
		s = s.replace(" xmlns=\"http://www.w3.org/1999/xhtml\"", "");
		return s;
	}

//	private static class ResourceLoaderUserAgent extends ITextUserAgent {
//		public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
//			super(outputDevice);
//		}
//
//		protected InputStream resolveAndOpenStream(String uri) {
//			System.err.println("buscando recurso externo: " + uri);
//
//			String entry = "pdf-resource-" + uri;
//
//			byte ab[] = (byte[]) Dao.syncCache().get(entry);
//			if (ab == null) {
//				try {
//					System.err.println("fazendo o download de recurso externo: " + uri);
//					ab = HttpGAE.fetch(new URL(uri));
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}
//				Dao.syncCache().put(entry, ab);
//			}
//
//			System.err.println("retornando recurso externo: " + uri);
//			ByteArrayInputStream bais = new ByteArrayInputStream(ab);
//			return bais;
//		}
//	}

	public static String extraiBody(final String sSource) {
		Document d = Jsoup.parse(sSource);
		return d.body().toString();
	}

	public byte[] converter(String sHtml) throws Exception {
		System.err.println("iniciando a conversão");
		String pagina = null;
		pagina = Utils.convertStreamToString(this.getClass().getResourceAsStream("pagina.html"));
		System.err.println("resource carregado");
		if (sHtml != null && pagina != null) {
			String prefix = pagina.substring(0, pagina.indexOf("<body>") + 6);
			String sufix = pagina.substring(pagina.indexOf("</body>"));

			sHtml = cleanHtml(prefix + extraiBody(sHtml) + sufix);
			System.err.println("HTML calculado");
		} else {
			sHtml = cleanHtml(sHtml);
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			// Habilitar para carregar recursos externos
			// ResourceLoaderUserAgent callback = new
			// ResourceLoaderUserAgent(renderer.getOutputDevice());
			// callback.setSharedContext(renderer.getSharedContext());
			// renderer.getSharedContext().setUserAgentCallback(callback);

			renderer.setDocument(sHtml.getBytes(StandardCharsets.UTF_8));
			renderer.layout();
			renderer.createPDF(baos);
			System.err.println("PDF gerado");

			return baos.toByteArray();
		}
	}
}

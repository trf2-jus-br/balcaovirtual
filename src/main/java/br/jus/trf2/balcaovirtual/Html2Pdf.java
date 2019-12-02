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

	static String cleanHtmlJSoup(String html, boolean fBodyOnly) {
		if (html.startsWith("<?xml "))
			return html;
		final Document document = Jsoup.parse(html);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		document.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
		if (fBodyOnly)
			return document.body().html();
		else
			return document.html();
	}

	public String cleanHtml(String data) throws UnsupportedEncodingException {
		if (data.startsWith("<?xml "))
			return data;

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

	public byte[] converter(String sHtml, boolean apenasBody) throws Exception {
		System.err.println("iniciando a conversão");
		String pagina = null;
		if (apenasBody)
			pagina = Utils.convertStreamToString(this.getClass().getResourceAsStream("pagina.html"));
		System.err.println("resource carregado");
		if (sHtml != null && pagina != null) {
			String prefix = pagina.substring(0, pagina.indexOf("<body>"));
			String sufix = pagina.substring(pagina.indexOf("</body>") + 7);

			sHtml = cleanHtml(prefix + extraiBody(sHtml) + sufix);
			System.err.println("HTML calculado");
		} else {
			sHtml = cleanHtml(sHtml);
		}
		System.err.println(sHtml);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			// Habilitar para carregar recursos externos
			// ResourceLoaderUserAgent callback = new
			// ResourceLoaderUserAgent(renderer.getOutputDevice());
			// callback.setSharedContext(renderer.getSharedContext());
			// renderer.getSharedContext().setUserAgentCallback(callback);

			renderer.setDocumentFromString(sHtml);
			renderer.layout();
			renderer.createPDF(baos);
			System.err.println("PDF gerado");

			return baos.toByteArray();
		}
	}

	public byte[] converterJsoup(String sHtml, boolean apenasBody) throws Exception {
		System.err.println("iniciando a conversão");
		String pagina = null;
		sHtml = sHtml.replace("<u=\"\">", ">");
		sHtml = sHtml.replace(" <u><b>", "><b>");

		if (apenasBody)
			pagina = Utils.convertStreamToString(this.getClass().getResourceAsStream("pagina.html"));
		System.err.println("resource carregado");
		if (sHtml != null && pagina != null) {
			String prefix = pagina.substring(0, pagina.indexOf("<body>"));
			String sufix = pagina.substring(pagina.indexOf("</body>") + 7);

			sHtml = prefix + extraiBody(sHtml) + sufix;
			
			sHtml = cleanHtmlJSoup(sHtml, false);
			System.err.println("HTML calculado");
		} else {
			sHtml = cleanHtml(sHtml);
		}
		System.err.println(sHtml);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			// Habilitar para carregar recursos externos
			// ResourceLoaderUserAgent callback = new
			// ResourceLoaderUserAgent(renderer.getOutputDevice());
			// callback.setSharedContext(renderer.getSharedContext());
			// renderer.getSharedContext().setUserAgentCallback(callback);

			renderer.setDocumentFromString(sHtml);
			renderer.layout();
			renderer.createPDF(baos);
			System.err.println("PDF gerado");

			return baos.toByteArray();
		}
	}

}

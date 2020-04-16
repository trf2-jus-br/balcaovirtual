package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.crivano.swaggerservlet.PresentableException;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;

public class ProcessoCompleto implements Callable<String> {
	private String uuid;
	private String username;
	private String password;
	private String sistema;
	private String numProc;

	private Status status;

	public ProcessoCompleto(String filename, String username, String password, String sistema, String numProc) {
		super();
		this.uuid = filename;
		this.username = username;
		this.password = password;
		this.sistema = sistema;
		this.numProc = numProc;
	}

	@Override
	public String call() throws Exception {
		ContentInfoUtil contentInfoUtil = new ContentInfoUtil();
		String bufName = null;

		try {

			this.status = Status.update(this.uuid, "Obtendo a lista de documentos", 0, 100, 0L);

			// Consulta o processo para saber quais são os documentos a serem
			// concatenados
			String json = SoapMNI.consultarProcesso(username, password, sistema, numProc, false, false, true);

			JSONObject proc = new JSONObject(json).getJSONObject("value");
			JSONArray docs = proc.getJSONArray("documento");

			// Cria um documento em diretório temporário para agregar os
			// diversos PDFs
			String dirTemp = Utils.getDirTemp();
			bufName = dirTemp + "/" + numProc + "-completo-" + uuid + ".pdf";
			FileOutputStream buf = new FileOutputStream(bufName);
			Document document = new Document();
			PdfCopy copy = new PdfSmartCopy(document, buf);
			document.open();
			PdfReader reader;

			long bytes = 0;
			for (int i = 0; i < docs.length(); i++) {
				String idDocumento = docs.getJSONObject(docs.length() - i - 1).getString("idDocumento");

				this.status = Status.update(this.uuid, "Agregando documento " + (i + 1) + "/" + docs.length(),
						i * 2 + 1, docs.length() * 2 + 1, bytes);

				byte[] ab = SoapMNI.obterPecaProcessual(username, password, sistema, numProc, idDocumento);

//			byte[] ab = downloadUrl(new URL(
//					"http://balcaovirtualc.trf2.jus.br/balcaovirtual/api/v1/download/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzI0NTgzNTgsIm9yZ2FvIjoiYnIuanVzLmpmcmouZXByb2MiLCJpc3MiOiJ0cmYyLmp1cy5iciIsIm9yaWdpbiI6InB1YiIsImRvYyI6IjUxMTU2NTg4MjU3Mzg0NDAzMDc2NzIwOTQwODc4NSIsInR5cCI6ImRvd25sb2FkIiwiaWF0IjoxNTcyNDU3NzU4LCJwcm9jIjoiNTAwODgyMzczMjAxODQwMjUxMDEifQ.CoZKjDUABJwaZ1MSGu642dTybszZzscV6N7FxzBBOo0/50088237320184025101-peca-511565882573844030767209408785.pdf"));

				if (ab == null)
					continue;

				ContentInfo info = contentInfoUtil.findMatch(ab);

				if (info.getMimeType().startsWith("application/xml")) {
					this.status = Status.update(this.uuid,
							"Convertendo documento " + (i + 1) + "/" + docs.length() + " de XHTML para PDF", i * 2 + 2,
							docs.length() * 2 + 1, bytes);

					final XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance()
							.createXMLStreamReader(new ByteArrayInputStream(ab));
					String fileEncoding = xmlStreamReader.getEncoding();
					boolean fHtml = false;
					while (xmlStreamReader.hasNext()) {
						int eventType = xmlStreamReader.next();
						if (eventType == XMLStreamConstants.START_ELEMENT) {
							if (xmlStreamReader.getLocalName().equals("html"))
								fHtml = true;
							break;
						}
					}
					xmlStreamReader.close();
					if (fHtml) {
						String html = new String(ab, fileEncoding);
						System.out.println(html);
						ab = new Html2Pdf().converter(html, false);
						info = contentInfoUtil.findMimeTypeMatch("application/pdf");
					}
				}

				if (info.getMimeType().startsWith("text/html")) {
					this.status = Status.update(this.uuid,
							"Convertendo " + (i + 1) + "/" + docs.length() + " de HTML para PDF", i * 2 + 2,
							docs.length() * 2 + 1, bytes);
					String html = new String(ab, StandardCharsets.UTF_8);
					if (html.toLowerCase().contains("charset=windows-1252")
							|| html.toLowerCase().contains("iso-8859-1"))
						html = new String(ab, StandardCharsets.ISO_8859_1);
					System.out.println(html);
					ab = new Html2Pdf().converter(html, false);
					info = contentInfoUtil.findMimeTypeMatch("application/pdf");
				}

				if (!"application/pdf".equals(info.getMimeType()))
					throw new PresentableException("Não foi possível obter um PDF. (" + info.getMimeType() + ")");

				bytes += ab.length;

				reader = new PdfReader(ab);
				copy.addDocument(reader);
				reader.close();
			}
			document.close();

			this.status = Status.update(this.uuid, "PDF completo gerado", docs.length() * 2 + 1, docs.length() * 2 + 1,
					bytes);
		} catch (Exception ex) {
			this.status.ex = ex;
			Status.update(this.uuid, this.status);
		}
		return bufName;
	}

}

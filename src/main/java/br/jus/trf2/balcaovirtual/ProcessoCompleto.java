package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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

			boolean fEproc = sistema.contains(".eproc");

			// Consulta o processo para saber quais são os documentos a serem
			// concatenados
			String json = SoapMNI.consultarProcesso(username, password, sistema, numProc, false, fEproc, true);

			JSONObject proc = new JSONObject(json).getJSONObject("value");
			JSONArray docs = proc.getJSONArray("documento");

			// Cria um mapa de movimentos para facilitar a criação das páginas de separação
			Map<String, JSONObject> movs = new HashMap<>();
			if (fEproc) {
				JSONArray movimentos = proc.getJSONArray("movimento");
				for (int i = 0; i < movimentos.length(); i++) {
					JSONObject mov = movimentos.getJSONObject(i);
					movs.put(mov.getString("identificadorMovimento"), mov);
				}
			}

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
				String idDocumento = null;

				JSONObject doc;
				if (fEproc) {
					doc = docs.getJSONObject(docs.length() - i - 1);
					String movimento = doc.getString("movimento");
					String dataHora = Utils
							.formatarDataHoraMinutoSegundo(Utils.parsearApoloDataHoraMinuto(doc.getString("dataHora")));

					String usuario = null;
					String descricao = null;
					if (movs.containsKey(movimento)) {
						JSONObject mov = movs.get(movimento);
						if (mov != null) {
							JSONArray complementos = mov.getJSONArray("complemento");
							if (complementos != null) {
								for (int j = 0; j < complementos.length(); j++) {
									String complemento = complementos.getString(j);
									if (complemento != null && complemento.startsWith("Movimentado por: ")) {
										usuario = complemento.replace("Movimentado por: ", "");
									}
								}
							}
							if (mov.getJSONObject("movimentoLocal") != null)
								descricao = mov.getJSONObject("movimentoLocal").getString("descricao");
						}
					}

					String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><style type=\"text/css\">@media print {} @page {size: a4 portrait; margin-left: 2cm; margin-right: 2cm; margin-top: 2cm; margin-bottom: 2cm;background-color: lightyellow;}</style></head><body style=\"background-color: lightyellow;\">";
					// String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><style type=\"text/css\">@media print {background-color: lightyellow; @page{size: a4 portrait; margin-left:0cm; padding-right: 3cm; margin-top: 3cm; margin-bottom: 3cm;}}</style></head><body style=\"background-color: lightyellow;\">";
					html += "<p align=\"center\"><b>";
					html += i == 0 ? "CAPA DO PROCESSO" : "PÁGINA DE SEPARAÇÃO";
					html += "</b><br/><i>(Gerada automaticamente pelo Balcão Virtual.)</i><br/><br/><br/><br/><br/></p>";
					if (i == 0)
						html += "<h1 style=\"text-align: center;\">Processo N&ordm; " + Utils.formatarNumeroProcesso(numProc) + "</h1>";
					html += "<br/><br/><br/><h2 style=\"text-align: center;\">Evento " + movimento;
					html += "</h2><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>";
					html += "<p>Data: " + dataHora + "</p>";
					html += "<p>Número do Processo: " + Utils.formatarNumeroProcesso(numProc) + "</p>";
					if (movimento != null)
						html += "<p>Número do Evento: " + movimento + "</p>";
					if (usuario != null)
						html += "<p>Usuário: " + usuario + "</p>";
					if (descricao != null)
						html += "<p>Descrição do Evento: " + descricao + "</p>";
					html += "</body></html>";

					byte[] abSep = new Html2Pdf().converter(html, false);
					bytes += abSep.length;
					reader = new PdfReader(abSep);
					copy.addDocument(reader);
					reader.close();
				} else
					doc = docs.getJSONObject(i);
				idDocumento = doc.getString("idDocumento");

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

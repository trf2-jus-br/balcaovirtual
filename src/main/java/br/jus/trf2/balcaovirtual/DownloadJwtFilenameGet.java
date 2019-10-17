package br.jus.trf2.balcaovirtual;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.DownloadJwtFilenameGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IDownloadJwtFilenameGet;

public class DownloadJwtFilenameGet implements IDownloadJwtFilenameGet {

	@Override
	public void run(DownloadJwtFilenameGetRequest req, DownloadJwtFilenameGetResponse resp) throws Exception {
		Map<String, Object> map = verify(req.jwt);
		String username = (String) map.get("username");
		String password;
		if (username != null)
			password = SessionsCreatePost.decrypt((String) map.get("pwd"));
		else {
			username = SwaggerServlet.getProperty("public.username");
			password = SwaggerServlet.getProperty("public.password");
		}
		String name = (String) map.get("name");
		String file = (String) map.get("file");
		String numProc = (String) map.get("proc");
		String numDoc = (String) map.get("doc");
		String orgao = (String) map.get("orgao");
		String type = (String) map.get("typ");
		String text = (String) map.get("text");
		String cargo = (String) map.get("cargo");
		String empresa = (String) map.get("empresa");
		String unidade = (String) map.get("unidade");
		String disposition = "attachment".equals(req.disposition) ? "attachment" : "inline";
		if (!"download".equals(type))
			throw new Exception("Tipo de token JWT inválido");
		if (text != null) {
			byte[] pdf = ProcessoNumeroCotaPrevisaoPdfPost.criarPDF(name, numProc, text, cargo, empresa, unidade);
			resp.contentdisposition = "inline";
			resp.contentlength = (long) pdf.length;
			resp.contenttype = "application/pdf";
			resp.inputstream = new ByteArrayInputStream(pdf);
		} else if (file != null && file.equals("avisos-pendentes.xml")) {

			// TODO: IMPLEMENTAR
			// // Processo completo
			// Future<SwaggerAsyncResponse<UsuarioWebUsernameAvisoPendenteExportarGetResponse>>
			// future = SwaggerCall
			// .callAsync("obter XML de avisos", "Bearer " + req.jwt, "GET",
			// Utils.getWsProcessualUrl() + "/usuario-web/" +
			// map.get("username")
			// + "/aviso-pendente/exportar",
			// null, UsuarioWebUsernameAvisoPendenteExportarGetResponse.class);
			// SwaggerAsyncResponse<UsuarioWebUsernameAvisoPendenteExportarGetResponse>
			// sar = future.get();
			// if (sar.getException() != null)
			// throw sar.getException();
			// UsuarioWebUsernameAvisoPendenteExportarGetResponse r =
			// (UsuarioWebUsernameAvisoPendenteExportarGetResponse) sar
			// .getResp();
			// resp.contentdisposition = "attachment;filename=" +
			// map.get("username") + "-avisos-pendentes.xml";
			// resp.contentlength = r.contentlength;
			// resp.contenttype = r.contenttype;
			// resp.inputstream = r.inputstream;
		} else if (file != null && file.equals("avisos-confirmados.xml")) {
			// TODO: IMPLEMENTAR
			// Future<SwaggerAsyncResponse<UsuarioWebUsernameAvisoConfirmadoExportarGetResponse>>
			// future = SwaggerCall
			// .callAsync("obter XML de avisos confirmados", "Bearer " +
			// req.jwt, "GET",
			// Utils.getWsProcessualUrl() + "/usuario-web/" +
			// map.get("username")
			// + "/aviso-confirmado/exportar",
			// null,
			// UsuarioWebUsernameAvisoConfirmadoExportarGetResponse.class);
			// SwaggerAsyncResponse<UsuarioWebUsernameAvisoConfirmadoExportarGetResponse>
			// sar = future.get();
			// if (sar.getException() != null)
			// throw sar.getException();
			// UsuarioWebUsernameAvisoConfirmadoExportarGetResponse r =
			// (UsuarioWebUsernameAvisoConfirmadoExportarGetResponse) sar
			// .getResp();
			// resp.contentdisposition = "attachment;filename=" +
			// map.get("username") + "-avisos-confirmados.xml";
			// resp.contentlength = r.contentlength;
			// resp.contenttype = r.contenttype;
			// resp.inputstream = r.inputstream;
		} else {
			ContentInfoUtil contentInfoUtil = new ContentInfoUtil();
			if (numDoc != null) {

				byte[] ab = null;
				// Peça Processual
				ab = SoapMNI.obterPecaProcessual(username, password, orgao, numProc, numDoc);

				ContentInfo info = contentInfoUtil.findMatch(ab);
				resp.contenttype = info.getMimeType();

				if (info.getMimeType().startsWith("application/xml")) {
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
						info = contentInfoUtil.findMimeTypeMatch("text/html");
						resp.contenttype = info.getMimeType() + "; charset=" + fileEncoding;
					}
				}
				resp.contentdisposition = disposition + ";filename=" + numProc + "-peca-" + numDoc + "."
						+ info.getFileExtensions()[0];

				resp.contentlength = (long) ab.length;
				resp.inputstream = new ByteArrayInputStream(ab);
			} else {
				// Processo completo

				// Consulta o processo para saber quais são os documentos a serem
				// concatenados
				String json = SoapMNI.consultarProcesso(username, password, orgao, numProc, false, false, true);

				JSONObject proc = new JSONObject(json).getJSONObject("value");
				JSONArray docs = proc.getJSONArray("documento");

				// Cria um documento em diretório temporário para agregar os
				// diversos PDFs
				String dirTemp = Utils.getDirTemp();
				String bufName = dirTemp + "/" + numProc + "-completo-" + UUID.randomUUID().toString() + ".pdf";
				FileOutputStream buf = new FileOutputStream(bufName);
				Document document = new Document();
				PdfCopy copy = new PdfSmartCopy(document, buf);
				document.open();
				PdfReader reader;

				for (int i = 0; i < docs.length(); i++) {
					String idDocumento = docs.getJSONObject(i).getString("idDocumento");

					byte[] ab = SoapMNI.obterPecaProcessual(username, password, orgao, numProc, idDocumento);

					ContentInfo info = contentInfoUtil.findMatch(ab);
					if (!"application/pdf".equals(info.getMimeType())) {
						String html = new String(ab, StandardCharsets.UTF_8);
						if (html.toLowerCase().contains("charset=windows-1252"))
							html = new String(ab, StandardCharsets.ISO_8859_1);
						ab = new Html2Pdf().converter(html);
					}

					reader = new PdfReader(ab);
					copy.addDocument(reader);
					reader.close();
				}
				document.close();

				resp.contentdisposition = disposition + ";filename=" + numProc + "-completo.pdf";
				resp.contentlength = (long) new File(bufName).length();
				resp.contenttype = "application/pdf";
				resp.inputstream = new FileInputStream(bufName);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter arquivo";
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getJwtSecret());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	public static String jwt(String origin, String username, String password, String nome, String orgao,
			String processo, String documento, String arquivo, String texto, String cargo, String empresa,
			String unidade) throws Exception {
		final String issuer = Utils.getJwtIssuer();
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		// token expires in 10min or 12h
		final long exp = iat + (documento != null ? 10 * 60L : 12 * 60 * 60L);

		final JWTSigner signer = new JWTSigner(Utils.getJwtSecret());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);

		if (origin != null)
			claims.put("origin", origin);
		if (username != null)
			claims.put("username", username);
		if (password != null)
			claims.put("pwd", SessionsCreatePost.encrypt(password));
		if (nome != null)
			claims.put("name", nome);
		if (orgao != null)
			claims.put("orgao", orgao);
		if (processo != null)
			claims.put("proc", processo);
		if (documento != null)
			claims.put("doc", documento);
		if (arquivo != null)
			claims.put("file", arquivo);
		if (texto != null)
			claims.put("text", texto);
		if (cargo != null)
			claims.put("cargo", cargo);
		if (empresa != null)
			claims.put("empresa", empresa);
		if (unidade != null)
			claims.put("unidade", unidade);
		claims.put("typ", "download");

		final String jwt = signer.sign(claims);
		return jwt;
	}

}

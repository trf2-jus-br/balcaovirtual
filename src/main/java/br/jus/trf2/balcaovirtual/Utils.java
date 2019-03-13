package br.jus.trf2.balcaovirtual;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.crivano.swaggerservlet.SwaggerServlet;

public class Utils {
//	public static String getOrgaos() {
//		try {
//			return SwaggerUtils.getProperty("orgaos",
//					"Não foi possível localizar propriedade que configure a lista de órgãos.", false);
//		} catch (Exception e) {
//			throw new RuntimeException("Erro de configuração", e);
//		}
//
//	}

	public static String[] getSystems() {
		String systems = SwaggerServlet.getProperty("systems");
		if (systems == null)
			return null;
		return systems.split(",");
	}

	public static String getPassword(String system) {
		return SwaggerServlet.getProperty(system + ".password");
	}

	public static String getApiUrl(String system) {
		return SwaggerServlet.getProperty(system + ".api.url");
	}

	public static String getMniWsdlUrl(String system) {
		return SwaggerServlet.getProperty(system + ".mni.url");

	}

	public static String getMniWsdlEndpoint(String system) {
		return SwaggerServlet.getProperty(system + ".mni.endpoint");

	}

	public static String getWsDocumentalUrl() {
		return SwaggerServlet.getProperty("ws.documental.url");

	}

	public static String getDirTemp() {
		return SwaggerServlet.getProperty("upload.dir.temp");
	}

	public static String getDirFinal() {
		return SwaggerServlet.getProperty("upload.dir.final");
	}

	public static String getUsuariosRestritos() {
		return SwaggerServlet.getProperty("username.restriction");

	}

	public static String getJwtIssuer() {
		return "trf2.jus.br";
	}

	public static String getJwtSecret() {
		return SwaggerServlet.getProperty("jwt.secret");
	}

	public static String getAssijusEndpoint() {
		return SwaggerServlet.getProperty("assijus.endpoint");
	}

	public static String getAssijusSystemMovimentos() {
		return SwaggerServlet.getProperty("assijus.system.movimentos");
	}

	public static String getAssijusSystemExpedientes() {
		return SwaggerServlet.getProperty("assijus.system.expedientes");
	}

	/**
	 * Remove os acentos da string
	 * 
	 * @param acentuado - String acentuada
	 * @return String sem acentos
	 */
	public static String removeAcento(String acentuado) {
		if (acentuado == null)
			return null;
		String temp = new String(acentuado);
		temp = temp.replaceAll("[ÃÂÁÀ]", "A");
		temp = temp.replaceAll("[ÉÈÊ]", "E");
		temp = temp.replaceAll("[ÍÌÎ]", "I");
		temp = temp.replaceAll("[ÕÔÓÒ]", "O");
		temp = temp.replaceAll("[ÛÚÙÜ]", "U");
		temp = temp.replaceAll("[Ç]", "C");
		temp = temp.replaceAll("[ãâáà]", "a");
		temp = temp.replaceAll("[éèê]", "e");
		temp = temp.replaceAll("[íìî]", "i");
		temp = temp.replaceAll("[õôóò]", "o");
		temp = temp.replaceAll("[ûúùü]", "u");
		temp = temp.replaceAll("[ç]", "c");
		return temp;
	}

	public static String removePontuacao(String s) {
		if (s == null)
			return null;
		return s.replace("-", "").replace(".", "").replace("/", "");
	}

	private static final DateTimeFormatter dtfMNI = DateTimeFormat.forPattern("yyyyMMddHHmmss");

	public static String formatarApoloDataHoraMinuto(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfMNI);
	}

	public static Date parsearApoloDataHoraMinuto(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfMNI);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfBRHHMM = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

	public static String formatarDataHoraMinuto(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfBRHHMM);
	}

	public static Date parsearDataHoraMinuto(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfBRHHMM);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfBRHHMMSS = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

	public static String formatarDataHoraMinutoSegundo(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfBRHHMMSS);
	}

	public static Date parsearDataHoraMinutoSegundo(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfBRHHMMSS);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfBR = DateTimeFormat.forPattern("dd/MM/yyyy");

	public static String formatarData(Date d) {
		DateTime dt = new DateTime(d.getTime());
		return dt.toString(dtfBR);

	}

	public static Date parsearData(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfBR);
		return dt.toDate();
	}

	private static final DateTimeFormatter dtfJPHHMMSS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	public static Date parsearDataHoraFormatoJapones(String s) {
		if (s == null)
			return null;
		DateTime dt = DateTime.parse(s, dtfJPHHMMSS);
		return dt.toDate();
	}

	public static String formatarNumeroProcesso(String numProc) {
		String numProcFormated = numProc;
		try {
			numProcFormated = numProc.replaceAll("^(\\d{7})-?(\\d{2})\\.?(\\d{4})\\.?(4)\\.?(02)\\.?(\\d{4})(\\d{2})?",
					"$1-$2.$3.$4.$5.$6$7");
		} catch (Exception ex) {
		}
		return numProcFormated;
	}

	public static Date parsearDataHoraFormatoJS(String s) {
		if (s == null)
			return null;
		s = s.replace("T", " ");
		DateTime dt = DateTime.parse(s, dtfJPHHMMSS);
		return dt.toDate();
	}

	public static byte[] calcSha1(byte[] content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String makeSecret(String s) {
		if (s == null || s.length() == 0)
			return null;
		byte[] bytes = s.getBytes();
		return bytesToHex(calcSha1(bytes));
	}

	public static double parsearValor(String s) {
		if (s == null)
			return 0;
		s = s.trim();
		if (s.length() == 0)
			return 0;
		s = s.replace(".", "").replace(",", ".");
		return Double.parseDouble(s);
	}

	public static String convertStreamToString(java.io.InputStream is) {
		@SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String getOrgao(String system) {
		// TODO: Implementar esse método
		return null;
	}

}

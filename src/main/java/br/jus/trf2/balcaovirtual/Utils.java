package br.jus.trf2.balcaovirtual;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.crivano.swaggerservlet.SwaggerUtils;

public class Utils {
	public static String getOrgaos() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.orgaos",
					"Não foi possível localizar propriedade que configure a lista de órgãos.", false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getMniWsdlUrl(String orgao) {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.mni." + orgao.toLowerCase() + ".url",
					"Não foi possível localizar propriedade que configure a URL do MNI: " + "balcaovirtual.mni."
							+ orgao.toLowerCase() + ".url",
					false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getWsProcessualUrl() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.ws.processual.url",
					"Não foi possível localizar a propridade que configura a URL do webservice de integração com o sistema processual.",
					false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getWsDocumentalUrl() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.ws.documental.url",
					"Não foi possível localizar a propridade que configura a URL do webservice documental.", false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getDirTemp() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.upload.dir.temp",
					"Não foi configurado o diretório temporário dos PDFs", false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getDirFinal() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.upload.dir.final",
					"Não foi configurado o diretório de destino dos PDFs", false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getUsuariosRestritos() {
		try {
			return SwaggerUtils.getProperty("balcaovirtual.username.restriction", null);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}

	}

	public static String getJwtIssuer() {
		return "balcaovirtual.trf2.jus.br";
	}

	public static String getJwtSecret() {
		return SwaggerUtils.getProperty("balcaovirtual.jwt.secret", null);
	}

	public static String getAssijusEndpoint() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.assijus.endpoint",
					"Não foi configurada a URL do Assijus", false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}
	}

	public static String getAssijusSystemMovimentos() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.assijus.system.movimentos",
					"Não foi configurada o sistema de movimentos do Assijus", false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}
	}

	public static String getAssijusSystemExpedientes() {
		try {
			return SwaggerUtils.getRequiredProperty("balcaovirtual.assijus.system.expedientes",
					"Não foi configurada o sistema de expedientes do Assijus", false);
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}
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

}

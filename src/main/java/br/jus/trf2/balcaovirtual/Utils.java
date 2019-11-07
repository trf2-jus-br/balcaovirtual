package br.jus.trf2.balcaovirtual;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallStatus;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;
import com.crivano.swaggerservlet.SwaggerServlet;

public class Utils {
	public static final String HTML_START = "<!--HTML-START-->";
	public static final String HTML_END = "<!--HTML-END-->";
	public static final String ERROR_MESSAGE = "Mensagem-De-Erro";
	public static final String RESULT_KIND = "Tipo-De-Resultado";
	public static final String CERT_NUMBER = "Numero-Da-Certidao";
	public static final String NAME = "Nome-Da-Parte";
	public static final String QS = "QS";
	public static final String POST_PARAMS = "PostParams";

	// public static String getOrgaos() {
	// try {
	// return SwaggerUtils.getProperty("orgaos",
	// "Não foi possível localizar propriedade que configure a lista de
	// órgãos.", false);
	// } catch (Exception e) {
	// throw new RuntimeException("Erro de configuração", e);
	// }
	//
	// }

	public static String getBaseUrl() {
		return SwaggerServlet.getProperty("base.url");
	}

	public static String[] getSystems() {
		String systems = SwaggerServlet.getProperty("systems");
		if (systems == null)
			return null;
		return systems.split(",");
	}

	public static String getName(String system) {
		try {
			return SwaggerServlet.getProperty(system.toLowerCase() + ".name");
		} catch (Exception e) {
			throw new RuntimeException("Erro de configuração", e);
		}
	}

	public static String getSystemsNames() {
		String s = null;
		String[] systems = getSystems();
		int i = 0;
		for (String system : systems) {
			String name = SwaggerServlet.getProperty(system.toLowerCase() + ".name");
			if (i == 0)
				s = "";
			else if (i == systems.length - 1)
				s += " e ";
			else
				s += ", ";
			s += name;
			i++;
		}
		return s;
	}

	public static String getApiPassword(String system) {
		return SwaggerServlet.getProperty(system + ".api.password");
	}

	public static String getApiUrl(String system) {
		return SwaggerServlet.getProperty(system + ".api.url");
	}

	public static String getApiEprocUrl(String system) {
		return SwaggerServlet.getProperty(system + ".api.eproc.url");
	}

	public static String getApiEprocPassword(String system) {
		return SwaggerServlet.getProperty(system + ".api.eproc.password");
	}

	public static String getCertApiUrl(String system) {
		return SwaggerServlet.getProperty(system + ".cert.api.url");
	}

	public static String getCertApiPassword(String system) {
		return SwaggerServlet.getProperty(system + ".cert.api.password");
	}
	
	public static String getAssijusUrl(String system) {
		return SwaggerServlet.getProperty(system + ".assijus.url");
	}

	public static String getAssijusPassword(String system) {
		return SwaggerServlet.getProperty(system + ".assijus.password");
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

	public static byte[] calcSha256(byte[] content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
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
		return getName(system).split("/")[1];
	}

	private static class VerifyCaptchaRequest implements ISwaggerRequest {
		public String secret;
		public String response;
		public String remoteip;
	}

	private static class VerifyCaptchaResponse implements ISwaggerResponse {
		public boolean success;
		public String challenge_ts;
		public String apk_package_name;
	}

	// Se houver erro de SSL, pode ser necessário instalar o certificado do
	// Google na cadeia de certificados do Java, seguindo o exemplo abaixo:
	//
	// c:\OpenSSL-Win64\bin>openssl s_client -connect www.google.com:443 < NUL >
	// google.crt
	//
	// e:\Desenvolvimento\jre1.8.0_162\bin>keytool -import -alias
	// www.google.com -keystore ../lib/security/cacerts -file
	// c:\OpenSSL-Win64\bin\google.crt
	//
	public static boolean verifyCaptcha(String token) throws Exception {
		if (token == null)
			throw new PresentableException("Captcha não pode ser nulo");
		VerifyCaptchaRequest q = new VerifyCaptchaRequest();
		q.secret = SwaggerServlet.getProperty("recaptcha.secret.key");
		q.response = token;
		q.remoteip = SwaggerServlet.getHttpServletRequest().getRemoteAddr();
		VerifyCaptchaResponse r = SwaggerCall.doHTTP(null, "https://www.google.com/recaptcha/api/siteverify", "GET", q,
				VerifyCaptchaResponse.class);
		return r.success;
	}

	public static ArrayList<IBalcaoVirtual.ListStatus> getStatus(SwaggerMultipleCallResult mcr) {
		ArrayList<IBalcaoVirtual.ListStatus> l = new ArrayList<IBalcaoVirtual.ListStatus>();
		for (SwaggerCallStatus sts : mcr.status) {
			IBalcaoVirtual.ListStatus lsts = new IBalcaoVirtual.ListStatus();
			lsts.system = sts.system;
			lsts.errormsg = sts.errormsg;
			lsts.stacktrace = sts.stacktrace;
			l.add(lsts);
		}
		return l;
	}

	public static boolean isConsultaPublica(String idConsultante) {
		return idConsultante.equalsIgnoreCase(SwaggerServlet.getProperty("public.username"));
	}

	public static String limparHtml(String s, String tipoDeConteudo) throws PresentableException {
		if (!s.contains(Utils.HTML_START) || !s.contains(Utils.HTML_END))
			throw new PresentableException(
					"Não foi possível obter " + tipoDeConteudo + ", por favor tente novamente em alguns minutos.");
		if (s.contains(Utils.HTML_START))
			s = s.substring(s.indexOf(Utils.HTML_START) + Utils.HTML_START.length());
		if (s.contains(Utils.HTML_END))
			s = s.substring(0, s.indexOf(Utils.HTML_END));
		s = s.replaceAll(" width=\"[0-9]+\"", " width=\"100%\"");
		s = s.replaceAll("src=\"./imagens/brasao-apolo.jpg\"",
				"src=\"" + getBaseUrl() + "/assets/brasao-260x260.png\"");
		s = s.replaceAll("font-size: [0-9]+pt", "");
		s = s.replaceAll(" face=\"Times New Roman\"", "");
		s = s.replaceAll(" color=\"#[0-9]+\"", "");

		return s;
	}

	public static String obterHtml(String s, String tipo) throws PresentableException {
		switch (tipo) {
		case "AUTENTICADO":
			s = limparHtml(s, "dados de autenticidade certidão");
			break;
		case "POSITIVO":
			s = limparHtml(s, "a certidão");
			break;
		case "NEGATIVO":
			s = limparHtml(s, "a certidão negativa");
			break;
		case "REQUERIDO":
			s = limparHtml(s, "dados do requerimento da certidão");
			break;
		case "REQUERER":
			s = limparHtml(s, "dados para requerer a certidão");
			break;
		}
		return s;
	}

}

package br.jus.trf2.balcaovirtual;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerError;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoEmitirRequisitanteCpfcnpjPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoEmitirRequisitanteCpfcnpjPostResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ICertidaoEmitirRequisitanteCpfcnpjPost;

public class CertidaoEmitirRequisitanteCpfcnpjPost implements ICertidaoEmitirRequisitanteCpfcnpjPost {

	@Override
	public void run(CertidaoEmitirRequisitanteCpfcnpjPostRequest req,
			CertidaoEmitirRequisitanteCpfcnpjPostResponse resp) throws Exception {
		if (!CertidaoObterTokenGet.isValidToken(req.token, null, req.requisitante, req.cpfcnpj))
			throw new PresentableException("Token inválido");

		FetchResponse r = fetch("http://10.13.1.6/Homologacao/certidao/crivano_emissao_cert.asp", "Teste123", "POST",
				"Botao=Emitir&CPFReq=" + req.requisitante + "&NumDocPess=" + req.cpfcnpj);

		if (r.headerFields.containsKey(Utils.ERROR_MESSAGE))
			throw new PresentableUnloggedException(r.headerFields.get(Utils.ERROR_MESSAGE).get(0));

		resp.html = r.html;

		if (r.headerFields.containsKey(Utils.RESULT_KIND)) {
			resp.tipo = r.headerFields.get(Utils.RESULT_KIND).get(0);

			switch (resp.tipo) {
			case "NEGATIVO":
				if (!resp.html.contains(Utils.CERT_INI) || !resp.html.contains(Utils.CERT_FIM))
					throw new PresentableException(
							"Não foi possível obter a certidão negativa, por favor tente novamente em alguns minutos.");
				if (resp.html.contains(Utils.CERT_INI))
					resp.html = resp.html.substring(resp.html.indexOf(Utils.CERT_INI) + Utils.CERT_INI.length());
				if (resp.html.contains(Utils.CERT_FIM))
					resp.html = resp.html.substring(0, resp.html.indexOf(Utils.CERT_FIM));
				resp.html = resp.html.replaceAll(" width=\"[0-9]+\"", " width=\"100%\"");
				break;
			case "REQUERER":
				if (!resp.html.contains(Utils.REQ_INI) || !resp.html.contains(Utils.REQ_FIM))
					throw new PresentableException(
							"Não foi possível obter dados para requerer a certidão, por favor tente novamente em alguns minutos.");
				if (resp.html.contains(Utils.REQ_INI))
					resp.html = resp.html.substring(resp.html.indexOf(Utils.REQ_INI) + Utils.REQ_INI.length());
				if (resp.html.contains(Utils.REQ_FIM))
					resp.html = resp.html.substring(0, resp.html.indexOf(Utils.REQ_FIM));
				resp.html = resp.html.replaceAll(" width=\"[0-9]+\"", " width=\"100%\"");
				break;
			}
		}
		if (r.headerFields.containsKey(Utils.CERT_NUMBER))
			resp.numero = r.headerFields.get(Utils.CERT_NUMBER).get(0);

		if (r.headerFields.containsKey(Utils.NAME))
			resp.nome = r.headerFields.get(Utils.NAME).get(0);

		if (r.headerFields.containsKey(Utils.QS))
			resp.qs = r.headerFields.get(Utils.QS).get(0);

		if (r.headerFields.containsKey(Utils.POST_PARAMS))
			resp.params = r.headerFields.get(Utils.POST_PARAMS).get(0);

	}

	@Override
	public String getContext() {
		return "emitir certidão";
	}

	public static class FetchResponse {
		String html;
		Map<String, List<String>> headerFields;
	}

	public static FetchResponse fetch(String url, String authorization, String method, String body) throws Exception {
		HttpURLConnection con = null;
		URL obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();

		con.setRequestProperty("User-Agent", "SwaggerServlets");
		if (authorization != null)
			con.setRequestProperty("Authorization", authorization);
		con.setRequestMethod(method);

		if ("POST".equals(method)) {
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setConnectTimeout(5000); // set timeout to 5 seconds
			con.setReadTimeout(20000); // set read timeout to 20 seconds
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(body.getBytes(StandardCharsets.UTF_8));
			wr.flush();
			wr.close();
		}

		con.setInstanceFollowRedirects(false);

		int responseCode = con.getResponseCode();

		while (responseCode == 302) {
			String newUrl = con.getHeaderField("Location");
			String cookies = con.getHeaderField("Set-Cookie");

			if (!newUrl.startsWith("http://") && !newUrl.startsWith("https://"))
				newUrl = url.substring(0, url.lastIndexOf("/") + 1) + newUrl;

			URL obj2 = new URL(newUrl);
			con = (HttpURLConnection) obj2.openConnection();
			con.setRequestProperty("Cookie", cookies);
			con.setRequestProperty("User-Agent", "SwaggerServlets");
			if (authorization != null)
				con.setRequestProperty("Authorization", authorization);
			con.setRequestMethod("GET");
			responseCode = con.getResponseCode();
		}

		if (responseCode >= 400 && responseCode < 600) {
			SwaggerError err = null;
			InputStream errorStream = null;
			String string = null;
			errorStream = con.getErrorStream();
			if (errorStream != null)
				string = convertStreamToString(errorStream);
			String errormsg = "HTTP ERROR: " + Integer.toString(responseCode) + " - " + string;
			if (con.getResponseMessage() != null)
				errormsg = errormsg + " - " + con.getResponseMessage();
			errormsg = errormsg.replaceAll("\\s+", " ");
			throw new PresentableUnloggedException(errormsg);
		}

		FetchResponse resp = new FetchResponse();
		resp.html = convertStreamToString(con.getInputStream());
		resp.headerFields = con.getHeaderFields();
		return resp;
	}

	public static String convertStreamToString(java.io.InputStream is) {
		@SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is, "ISO-8859-1").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}

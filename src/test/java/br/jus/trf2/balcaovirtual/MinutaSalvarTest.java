package br.jus.trf2.balcaovirtual;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
//import org.joda.time.LocalDate;

//import br.jus.trf2.balcaovirtual.Utils;

public class MinutaSalvarTest {

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
	
	public static byte[] getParametros() {
		byte[]  postDataBytes =null;
		try {
		Map<String,Object> params = new LinkedHashMap<>();
	        params.put("id_minuta", "123456789");
	        params.put("text", "Algum texto");
	        params.put("alterarStatus","10394");
	        params.put("statusMinutaDesejado", "4");
	        params.put("sbmCadastrarVersaoConteudo", "S");
	        params.put("acao", "minuta_salvar");
	        params.put("cod_tipo_salvamento_versao_conteudo", "4");
	        params.put("tamSecEditaveis", "10");
	        

	        StringBuilder postData = new StringBuilder();
	        for (Map.Entry<String,Object> param : params.entrySet()) {
	            if (postData.length() != 0) postData.append('&');
	            
					postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			
	            postData.append('=');
	            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        postDataBytes = postData.toString().getBytes("UTF-8");
	      
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		return postDataBytes;
	}
	public static String getMinutaSalvar (String url) {
	 
		String jsonResponse = null;
	 
	    if (url!=null) {
	        try {
	           
	            URL object = new URL(url);

	            HttpURLConnection connection = (HttpURLConnection) object
	                    .openConnection();
	            // int timeOut = connection.getReadTimeout();
	            connection.setReadTimeout(60 * 1000);
	            connection.setConnectTimeout(60 * 1000);
	           String  senha = "CP.7RFii";
	           LocalDate hoje = LocalDate.now();
	           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	           String hojeFormatado = hoje.format(formatter);
	           String dataSenha = hojeFormatado + senha;
			   String hashSenha=bytesToHex(calcSha256(dataSenha.getBytes(StandardCharsets.US_ASCII))).toLowerCase();
	           connection.setRequestProperty("Authorization", hashSenha);
	           connection.setDoOutput(true);
	           connection.getOutputStream().write(getParametros());
	           int responseCode = connection.getResponseCode();
	           String responseMsg = connection.getResponseMessage();

	            if (responseCode == 200) {
	                InputStream inputStr = connection.getInputStream();
	                String encoding = connection.getContentEncoding() == null ? "UTF-8"
	                        : connection.getContentEncoding();
	               jsonResponse = IOUtils.toString(inputStr, encoding);
	          
	            }
	        } catch (Exception e) {
	            e.printStackTrace();

	        }
	    }
	    return jsonResponse;
	    
	}
	
	public static void main (String[] args) {
		
		//https://eproc-homologacao.jfrj.jus.br/eproc/controlador_ajax.php?acao_ajax=minuta_salvar&acao_origem=minuta_editar&hash=972547f8d587c26b8c1a84f2ea128107
		
	System.out.print(getMinutaSalvar("https://eproc-homologacao.jfrj.jus.br/eproc/controlador_rest.php/balcaovirtual/minuta/salvar"));
		
	}


}

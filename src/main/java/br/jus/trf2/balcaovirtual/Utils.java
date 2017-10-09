package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import com.crivano.swaggerservlet.SwaggerUtils;

public class Utils {

	public static boolean getMarcasAtivas() {
		return Boolean.parseBoolean(SwaggerUtils.getProperty("balcaovirtual.marcas", "false"));
	}

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

	/**
	 * Remove os acentos da string
	 * 
	 * @param acentuado
	 *            - String acentuada
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
}

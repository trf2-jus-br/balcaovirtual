package br.jus.trf2.balcaovirtual;

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
		return SwaggerUtils.getProperty("balcaovirtual.jwt.secret", null);
	}

	public static String getJwtSecret() {
		return SwaggerUtils.getProperty("balcaovirtual.jwt.secret", null);
	}

}

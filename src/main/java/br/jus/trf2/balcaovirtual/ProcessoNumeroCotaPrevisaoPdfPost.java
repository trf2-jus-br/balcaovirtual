package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroCotaPrevisaoPdfPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaPrevisaoPdfPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaPrevisaoPdfPostResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;

public class ProcessoNumeroCotaPrevisaoPdfPost implements IProcessoNumeroCotaPrevisaoPdfPost {

	@Override
	public void run(ProcessoNumeroCotaPrevisaoPdfPostRequest req, ProcessoNumeroCotaPrevisaoPdfPostResponse resp)
			throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();
		resp.jwt = DownloadJwtFilenameGet.jwt(u.origem, u.usuario, u.senha, u.nome, req.sistema, req.numero, null, null,
				req.texto, req.cargo, req.empresa, req.unidade, null);
	}

	public static byte[] criarPDF(String nome, String numero, String texto, String cargo, String empresa,
			String unidade) throws IOException, Exception, InterruptedException, ExecutionException {
		String data = null;
		try (Dao dao = new Dao()) {
			data = Utils.formatarDataHoraMinuto(dao.obtemData());
		}

		// CODFISH CODE para resolver um problema específico do TRF2
		if (unidade != null && unidade.contains(" - Gabinete"))
			unidade = unidade.split(" - Gabinete")[0];

		String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><style type=\"text/css\">@page {margin-left: 3cm; margin-right: 3cm; margin-top: 3cm; margin-bottom: 3cm;}</style></head><body>";
		html += "<p align=\"center\">Cota nos autos do Processo " + Utils.formatarNumeroProcesso(numero)
				+ "</p><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>";
		html += "<p>MM. Juizo - " + unidade + ",<br/><br/></p>";
		// html += "<table width=\"100%\"><tr><td align=\"left\"><br/><p
		// style=\"line-height: 2cm;\">" + texto +
		// "</p><br/><br/></td></tr></table>";
		html += "<p>" + texto + "</p>";
		html += "<br/><p align=\"left\">" + nome + "<br/>" + cargo + "<br/>" + empresa + "</p>";
		html += "</body></html>";

		return new Html2Pdf().converter(html, true);
	}

	@Override
	public String getContext() {
		return "enviar cota";
	}

}

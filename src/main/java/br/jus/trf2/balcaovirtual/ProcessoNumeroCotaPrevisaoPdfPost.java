package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroCotaPrevisaoPdfPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaPrevisaoPdfPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotaPrevisaoPdfPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemadocumental.ISistemaDocumental.HtmlPdfPostRequest;
import br.jus.trf2.sistemadocumental.ISistemaDocumental.HtmlPdfPostResponse;

public class ProcessoNumeroCotaPrevisaoPdfPost implements IProcessoNumeroCotaPrevisaoPdfPost {

	@Override
	public void run(ProcessoNumeroCotaPrevisaoPdfPostRequest req, ProcessoNumeroCotaPrevisaoPdfPostResponse resp)
			throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		resp.jwt = DownloadJwtFilenameGet.jwt(u.origem, u.usuario, u.senha, u.nome, req.sistema, req.numero, null, null,
				req.texto, req.cargo, req.empresa, req.unidade);
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

		HtmlPdfPostRequest q = new HtmlPdfPostRequest();
		q.html = html;
		q.conv = "0";
		Future<SwaggerAsyncResponse<HtmlPdfPostResponse>> future = SwaggerCall.callAsync("converter HTML em PDF", null,
				"POST", Utils.getWsDocumentalUrl() + "/html-pdf", q, HtmlPdfPostResponse.class);
		SwaggerAsyncResponse<HtmlPdfPostResponse> sar = future.get();
		if (sar.getException() != null)
			throw new Exception(sar.getException().getLocalizedMessage(), sar.getException());
		HtmlPdfPostResponse r = (HtmlPdfPostResponse) sar.getResp();
		return r.pdf;
	}

	@Override
	public String getContext() {
		return "enviar cota";
	}

}

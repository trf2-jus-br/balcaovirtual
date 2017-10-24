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
		resp.jwt = DownloadJwtFilenameGet.jwt(u.origem, u.usuario, u.nome, req.orgao, req.numero, null, null,
				req.texto);
	}

	public static byte[] criarPDF(String nome, String numero, String texto)
			throws IOException, Exception, InterruptedException, ExecutionException {
		String data = null;
		try (Dao dao = new Dao()) {
			data = Utils.formatarDataHoraMinuto(dao.obtemData());
		}

		String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><style type=\"text/css\">@page {margin-left: 3cm; margin-right: 3cm; margin-top: 3cm; margin-bottom: 3cm;}</style></head><body>";
		html += "<p align=\"center\">Cota nos autos do Processo " + Utils.formatarNumeroProcesso(numero)
				+ "</p><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>";
		html += "<table width=\"100%\"><tr><td align=\"center\"><br/>" + texto + "<br/><br/></td></tr></table>";
		html += "<p align=\"right\">" + nome + " - " + data + "</p>";
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

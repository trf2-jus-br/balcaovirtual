package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroCotarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroCotarPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.UsuarioDetalhe;
import br.jus.trf2.sistemadocumental.ISistemaDocumental.HtmlPdfPostRequest;
import br.jus.trf2.sistemadocumental.ISistemaDocumental.HtmlPdfPostResponse;

public class ProcessoNumeroCotarPost implements IProcessoNumeroCotarPost {

	@Override
	public void run(ProcessoNumeroCotarPostRequest req, ProcessoNumeroCotarPostResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		Usuario u = SessionsCreatePost.assertUsuario();
		UsuarioDetalhe ud = u.usuarios.get(req.orgao.toLowerCase());
		String data = null;

		String tipo = SwaggerUtils.getRequiredProperty("balcaovirtual." + req.orgao.toLowerCase() + ".cota.tipo",
				"Parâmetro de tipo de documento não configurado", true);

		try (Dao dao = new Dao()) {
			data = Utils.formatarDataHoraMinuto(dao.obtemData());
		}

		String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><style type=\"text/css\">@page {margin-left: 3cm; margin-right: 3cm; margin-top: 3cm; margin-bottom: 3cm;}</style></head><body>";
		html += "<p align=\"center\">Cota nos autos do Processo " + Utils.formatarNumeroProcesso(req.numero)
				+ "</p><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>";
		html += "<table width=\"100%\"><tr><td align=\"center\"><br/>" + req.texto + "<br/><br/></td></tr></table>";
		html += "<p align=\"right\">" + u.nome + " - " + data + "</p>";
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

		byte[] pdf = r.pdf;

		String mensagem = SoapMNI.enviarPeticaoIntercorrente(authorization, req.orgao, req.numero, tipo,
				Integer.parseInt(req.nivelsigilo), null, pdf);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar cota";
	}

}

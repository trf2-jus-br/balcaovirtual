package br.jus.trf2.balcaovirtual;

import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
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
		String dataHora = null;

		String tipo = SwaggerUtils.getRequiredProperty("balcaovirtual." + req.orgao.toLowerCase() + ".cota.tipo",
				"Parâmetro de tipo de documento não configurado", true);

		try (Dao dao = new Dao()) {
			dataHora = Utils.formatarDataHoraMinuto(dao.obtemData());
		}
		
		 <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
         <head>
             <style type="text/css">
                 @page {
                     margin-left: 3cm;
                     margin-right: 3cm;
                     margin-top: 3cm;
                     margin-bottom: 3cm;
                 }
             </style>
         </head>
         <body>
             <p align="center">Cota nos autos do Processo 0000588-48.2004.4.02.5117</p><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><table width="100%"><tr><td align="center"><br/>Ciente. Nada a opor.<br/><br/></td></tr></table><p align="right">Giovanni da Silva de Souza - 19/10/2017</p>
         </body>
     </html>

		String html = "<p align=\"center\">" + req.texto + "</p><p>" + dataHora + "</p><p>" + u.nome + "</p>";

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

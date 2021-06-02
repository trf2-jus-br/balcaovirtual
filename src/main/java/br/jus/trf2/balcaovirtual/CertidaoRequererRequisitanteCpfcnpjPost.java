package br.jus.trf2.balcaovirtual;

import java.net.URLEncoder;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.CertidaoEmitirRequisitanteCpfcnpjPost.FetchResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ICertidaoRequererRequisitanteCpfcnpjPost;
import br.jus.trf2.balcaovirtual.util.AcessoPublico;

@AcessoPublico
public class CertidaoRequererRequisitanteCpfcnpjPost implements ICertidaoRequererRequisitanteCpfcnpjPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		if (!CertidaoObterTokenGet.isValidToken(req.token, null, req.requisitante, req.cpfcnpj))
			throw new PresentableException("Token inválido");

		FetchResponse r = CertidaoEmitirRequisitanteCpfcnpjPost.fetch(
				Utils.getCertApiUrl(req.sistema) + "/bv_conf_req_cert.asp", Utils.getCertApiPassword(req.sistema),
				"POST", "Botao=Requerer&NumDocPessReq=" + req.requisitante + "&NumDoc=" + req.cpfcnpj + "&Nome="
						+ URLEncoder.encode(req.nome, "ISO-8859-1"));

		if (r.headerFields.containsKey(Utils.ERROR_MESSAGE))
			throw new PresentableUnloggedException(r.headerFields.get(Utils.ERROR_MESSAGE).get(0));

		resp.html = r.html;

		if (r.headerFields.containsKey(Utils.RESULT_KIND)) {
			resp.tipo = r.headerFields.get(Utils.RESULT_KIND).get(0);
			resp.html = Utils.obterHtml(resp.html, resp.tipo);
		}

		if (r.headerFields.containsKey(Utils.CERT_NUMBER))
			resp.numero = r.headerFields.get(Utils.CERT_NUMBER).get(0);

	}

	@Override
	public String getContext() {
		return "requerer certidão";
	}

}

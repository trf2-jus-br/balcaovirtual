package br.jus.trf2.balcaovirtual;

import java.net.URLEncoder;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.CertidaoEmitirRequisitanteCpfcnpjPost.FetchResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoRequererRequisitanteCpfcnpjPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoRequererRequisitanteCpfcnpjPostResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ICertidaoRequererRequisitanteCpfcnpjPost;

public class CertidaoRequererRequisitanteCpfcnpjPost implements ICertidaoRequererRequisitanteCpfcnpjPost {

	@Override
	public void run(CertidaoRequererRequisitanteCpfcnpjPostRequest req,
			CertidaoRequererRequisitanteCpfcnpjPostResponse resp) throws Exception {
		if (!CertidaoObterTokenGet.isValidToken(req.token, null, req.requisitante, req.cpfcnpj))
			throw new PresentableException("Token inválido");

		FetchResponse r = CertidaoEmitirRequisitanteCpfcnpjPost.fetch("http://10.13.1.6/Homologacao/certidao/crivano_conf_req_cert.asp", "Teste123", "POST",
				"Botao=Requerer&NumDocPessReq=" + req.requisitante + "&NumDoc=" + req.cpfcnpj + "&Nome="
						+ URLEncoder.encode(req.nome, "ISO-8859-1"));

		if (r.headerFields.containsKey(Utils.ERROR_MESSAGE))
			throw new PresentableUnloggedException(r.headerFields.get(Utils.ERROR_MESSAGE).get(0));

		resp.html = r.html;

		if (r.headerFields.containsKey(Utils.RESULT_KIND)) {
			resp.tipo = r.headerFields.get(Utils.RESULT_KIND).get(0);

			switch (resp.tipo) {
			case "REQUERIDO":
				if (!resp.html.contains(Utils.REQ_INI) || !resp.html.contains(Utils.REQ_FIM))
					throw new PresentableException(
							"Não foi possível obter dados do requerimento certidão, por favor tente novamente em alguns minutos.");
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

	}

	@Override
	public String getContext() {
		return "requerer certidão";
	}

}

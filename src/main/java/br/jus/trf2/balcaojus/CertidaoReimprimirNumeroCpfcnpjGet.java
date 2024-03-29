package br.jus.trf2.balcaojus;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaojus.CertidaoEmitirRequisitanteCpfcnpjPost.FetchResponse;
import br.jus.trf2.balcaojus.IBalcaojus.ICertidaoReimprimirNumeroCpfcnpjGet;
import br.jus.trf2.balcaojus.util.AcessoPublico;

@AcessoPublico
public class CertidaoReimprimirNumeroCpfcnpjGet implements ICertidaoReimprimirNumeroCpfcnpjGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		if (!CertidaoObterTokenGet.isValidToken(req.token, req.numero, null, req.cpfcnpj))
			throw new PresentableException("Token inválido");

		FetchResponse r = CertidaoEmitirRequisitanteCpfcnpjPost.fetch(
				Utils.getCertApiUrl(req.sistema) + "/bv_cons_cert_lib.asp", Utils.getCertApiPassword(req.sistema),
				"POST", "Botao=Emitir&NumProt=" + req.numero + "&NumDocPess=" + req.cpfcnpj);

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
		return "autenticar certidão";
	}
}

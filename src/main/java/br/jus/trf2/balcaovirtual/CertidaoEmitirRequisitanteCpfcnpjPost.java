package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoEmitirRequisitanteCpfcnpjPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoEmitirRequisitanteCpfcnpjPostResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ICertidaoEmitirRequisitanteCpfcnpjPost;

public class CertidaoEmitirRequisitanteCpfcnpjPost implements ICertidaoEmitirRequisitanteCpfcnpjPost {

	@Override
	public void run(CertidaoEmitirRequisitanteCpfcnpjPostRequest req,
			CertidaoEmitirRequisitanteCpfcnpjPostResponse resp) throws Exception {
		if (!CertidaoObterTokenGet.isValidToken(req.token, null, req.requisitante, req.cpfcnpj))
			throw new PresentableException("Token inválido");
		resp.tipo = "NADA_CONSTA";
		resp.numero = "12345678";
		resp.html = "<p>Oi!</p>";
	}

	@Override
	public String getContext() {
		return "emitir certidão";
	}
}

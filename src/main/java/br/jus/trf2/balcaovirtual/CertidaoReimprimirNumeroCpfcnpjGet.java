package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoReimprimirNumeroCpfcnpjGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoReimprimirNumeroCpfcnpjGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ICertidaoReimprimirNumeroCpfcnpjGet;

public class CertidaoReimprimirNumeroCpfcnpjGet implements ICertidaoReimprimirNumeroCpfcnpjGet {

	@Override
	public void run(CertidaoReimprimirNumeroCpfcnpjGetRequest req, CertidaoReimprimirNumeroCpfcnpjGetResponse resp)
			throws Exception {
		if (!CertidaoObterTokenGet.isValidToken(req.token, req.numero, null, req.cpfcnpj))
			throw new PresentableException("Token inválido");
		resp.tipo = "NADA_CONSTA";
		resp.numero = "12345678";
		resp.html = "<p>Oi!</p>";
	}

	@Override
	public String getContext() {
		return "autenticar certidão";
	}
}

package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoAutenticarNumeroCpfcnpjGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoAutenticarNumeroCpfcnpjGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ICertidaoAutenticarNumeroCpfcnpjGet;

public class CertidaoAutenticarNumeroCpfcnpjGet implements ICertidaoAutenticarNumeroCpfcnpjGet {

	@Override
	public void run(CertidaoAutenticarNumeroCpfcnpjGetRequest req, CertidaoAutenticarNumeroCpfcnpjGetResponse resp)
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

package br.jus.trf2.balcaovirtual;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoObterTokenGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.CertidaoObterTokenGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ICertidaoObterTokenGet;
import br.jus.trf2.balcaovirtual.util.AcessoPublico;

@AcessoPublico
public class CertidaoObterTokenGet implements ICertidaoObterTokenGet {

	@Override
	public void run(CertidaoObterTokenGetRequest req, CertidaoObterTokenGetResponse resp) throws Exception {
		if (!Utils.verifyCaptcha(req.captcha))
			throw new PresentableUnloggedException("Token de reCaptcha inválido");
		resp.token = jwt(req.numero, req.requisitante, req.cpfcnpj);
	}

	@Override
	public String getContext() {
		return "obter token para acesso a certidão";
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getApiPassword());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	public static String jwt(String certidao, String requisitante, String cpfcnpj) throws Exception {
		final String issuer = Utils.getJwtIssuer();
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		// token expires in 12h
		final long exp = iat + 12 * 60 * 60L;

		final JWTSigner signer = new JWTSigner(Utils.getApiPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("certidao", certidao);
		claims.put("requisitante", requisitante);
		claims.put("cpfcnpj", cpfcnpj);

		claims.put("typ", "consulta-certidao");

		final String jwt = signer.sign(claims);
		return jwt;
	}

	public static boolean isValidToken(String token, String certidao, String requisitante, String cpfcnpj)
			throws PresentableUnloggedException {
		if (token == null || cpfcnpj == null || (certidao == null && requisitante == null))
			return false;
		Map<String, Object> m;
		try {
			m = verify(token);
		} catch (Exception ex) {
			throw new PresentableUnloggedException("Token de acesso a certidão inválido inválido", ex);
		}
		if (certidao != null && !m.get("certidao").equals(certidao))
			throw new PresentableUnloggedException("Token de acesso a certidão com número de certidão inválido");
		if (requisitante != null && !m.get("requisitante").equals(requisitante))
			throw new PresentableUnloggedException("Token de acesso a certidão com CPF de requisitante inválido");
		if (cpfcnpj != null && !m.get("cpfcnpj").equals(cpfcnpj))
			throw new PresentableUnloggedException("Token de acesso a certidão com número de CPF/CNPJ inválido");
		return true;
	}

}

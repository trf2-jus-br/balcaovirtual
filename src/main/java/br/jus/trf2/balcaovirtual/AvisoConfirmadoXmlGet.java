package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoXmlGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoXmlGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoConfirmadoXmlGet;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class AvisoConfirmadoXmlGet implements IAvisoConfirmadoXmlGet {

	@Override
	public void run(AvisoConfirmadoXmlGetRequest req, AvisoConfirmadoXmlGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		if (u.isInterno())
			throw new PresentableUnloggedException(
					"Exportação XML de avisos confirmados disponível apenas para usuários externos");
		resp.jwt = DownloadJwtFilenameGet.jwt(u.origem, u.senha, u.usuario, null, null, null, null,
				"avisos-confirmados.xml", null, null, null, null);
	}

	@Override
	public String getContext() {
		return "obter arquivo temporário";
	}

}

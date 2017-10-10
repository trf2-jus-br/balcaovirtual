package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoPendenteXmlGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoPendenteXmlGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoPendenteXmlGet;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class AvisoPendenteXmlGet implements IAvisoPendenteXmlGet {

	@Override
	public void run(AvisoPendenteXmlGetRequest req, AvisoPendenteXmlGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		if (u.isInterno())
			throw new PresentableUnloggedException("Exportação XML de avisos disponível apenas para usuários externos");
		resp.jwt = DownloadJwtFilenameGet.jwt(u.origem, u.usuario, null, null, null, "avisos-pendentes.xml");
	}

	@Override
	public String getContext() {
		return "obter arquivo temporário";
	}

}

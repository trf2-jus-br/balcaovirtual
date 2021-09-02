package br.jus.trf2.balcaojus;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IAvisoPendenteXmlGet;

public class AvisoPendenteXmlGet implements IAvisoPendenteXmlGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.isInterno())
			throw new PresentableUnloggedException("Exportação XML de avisos disponível apenas para usuários externos");
		resp.jwt = DownloadJwtFilenameGet.jwt(u.origem, u.usuario, null, null, null, null, "avisos-pendentes.xml", null,
				null, null, null, null);
	}

	@Override
	public String getContext() {
		return "obter arquivo temporário";
	}

}

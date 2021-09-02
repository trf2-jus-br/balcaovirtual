package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.ConfigAvisoPeticaoIntercorrente;
import br.jus.trf2.balcaojus.IBalcaojus.ConfigTipoPeticaoIntercorrente;
import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroPeticaoIntercorrenteValidarGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.AvisoPeticaoIntercorrente;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.TipoPeticaoIntercorrente;

public class ProcessoNumeroPeticaoIntercorrenteValidarGet implements IProcessoNumeroPeticaoIntercorrenteValidarGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		Future<SwaggerAsyncResponse<IUsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGet.Response>> future = SwaggerCall
				.callAsync("obter tipos de petição intercorrente", Utils.getApiPassword(req.sistema), "GET",
						Utils.getApiUrl(req.sistema) + "/usuario/" + u.usuario + "/processo/" + req.numero
								+ "/peticao-intercorrente/validar",
						null, IUsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGet.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGet.Response r = (IUsuarioUsernameProcessoNumeroPeticaoIntercorrenteValidarGet.Response) sar
				.getResp();

		if (r.tipos != null && r.tipos.size() > 0) {
			resp.tipos = new ArrayList<>();
			for (TipoPeticaoIntercorrente t : r.tipos) {
				ConfigTipoPeticaoIntercorrente tpi = new ConfigTipoPeticaoIntercorrente();
				tpi.sistema = req.sistema;
				tpi.id = t.id;
				tpi.descricao = t.descricao;
				resp.tipos.add(tpi);
			}
		}

		if (r.avisos != null && r.avisos.size() > 0) {
			resp.avisos = new ArrayList<>();
			for (AvisoPeticaoIntercorrente a : r.avisos) {
				ConfigAvisoPeticaoIntercorrente api = new ConfigAvisoPeticaoIntercorrente();
				api.id = a.id;
				api.evento = a.evento;
				api.data = a.data;
				resp.avisos.add(api);
			}
		}

		resp.identencerraprazos = r.identencerraprazos;
		resp.sigilo = r.sigilo;
		resp.parte = r.parte;
	}

	@Override
	public String getContext() {
		return "obter tipos de petição intercorrente";
	}

}

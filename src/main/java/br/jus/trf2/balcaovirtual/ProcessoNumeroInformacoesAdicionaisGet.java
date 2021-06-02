package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.AutenticarPost.UsuarioDetalhe;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroInformacoesAdicionaisGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameProcessoNumeroInformacoesAdicionaisGet;

public class ProcessoNumeroInformacoesAdicionaisGet implements IProcessoNumeroInformacoesAdicionaisGet {
	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			return;

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios == null)
			throw new PresentableException("Usuário não possui identificador e unidade");
		UsuarioDetalhe ud = u.usuarios.get(req.sistema.toLowerCase());

		if (!u.isInterno() && "prod".equals(BalcaoVirtualServlet.INSTANCE.getProperty("env"))
				&& (u.email == null || !u.email.endsWith("@pgfn.gov.br")))
			return;

		Future<SwaggerAsyncResponse<IUsuarioUsernameProcessoNumeroInformacoesAdicionaisGet.Response>> future = SwaggerCall
				.callAsync("obter informacoes adicionais", Utils.getApiPassword(req.sistema), "GET",
						Utils.getApiUrl(req.sistema) + "/usuario/" + u.usuario + "/processo/" + req.numero
								+ "/informacoes-adicionais",
						null, IUsuarioUsernameProcessoNumeroInformacoesAdicionaisGet.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameProcessoNumeroInformacoesAdicionaisGet.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameProcessoNumeroInformacoesAdicionaisGet.Response r = (IUsuarioUsernameProcessoNumeroInformacoesAdicionaisGet.Response) sar
				.getResp();

		resp.cdas = new ArrayList<>();
		if (r.cdas != null) {
			for (br.jus.trf2.sistemaprocessual.ISistemaProcessual.CDA cda : r.cdas) {
				br.jus.trf2.balcaovirtual.IBalcaoVirtual.CDA o = new br.jus.trf2.balcaovirtual.IBalcaoVirtual.CDA();
				o.numero = cda.numero;
				o.processoadministrativo = cda.processoadministrativo;
				o.status = cda.status;
				o.grupo = cda.grupo;
				o.codigotributo = cda.codigotributo;
				o.tributo = cda.tributo;
				o.valor = cda.valor;
				o.valorufir = cda.valorufir;
				o.dataorigem = cda.dataorigem;
				o.datainclusao = cda.datainclusao;
				resp.cdas.add(o);
			}
		}
	}

	@Override
	public String getContext() {
		return "consultar informações adicionais";
	}

}

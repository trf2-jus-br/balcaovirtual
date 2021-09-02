package br.jus.trf2.balcaojus;

import java.util.Random;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IMesaIdDocumentoId2AssinarComSenhaPost;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost;

public class MesaIdDocumentoId2AssinarComSenhaPost implements IMesaIdDocumentoId2AssinarComSenhaPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaojusServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		if (!u.usuario.equalsIgnoreCase(req.username))
			throw new PresentableUnloggedException("Login não confere");

		if (!u.getSenha().equals(req.password)) {
			Random r = new Random();
			Thread.sleep(2000 + r.nextInt(2000));
			throw new PresentableUnloggedException("Senha não confere");
		}

		IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost.Request q = new IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost.Request();
		Future<SwaggerAsyncResponse<IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost.Response>> future = SwaggerCall
				.callAsync(getContext(), Utils.getApiEprocPassword(req.sistema), "POST",
						Utils.getApiEprocUrl(req.sistema) + "/usuario/" + u.usuario + "/mesa/null/documento/" + req.id2
								+ "/assinar-com-senha",
						q, IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost.Response.class);
		SwaggerAsyncResponse<IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost.Response> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost.Response r = (IUsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPost.Response) sar
				.getResp();

		resp.status = r.status;
		resp.mensagem = r.errormsg;
	}

	@Override
	public String getContext() {
		return "assinar minuta com senha";
	}

}

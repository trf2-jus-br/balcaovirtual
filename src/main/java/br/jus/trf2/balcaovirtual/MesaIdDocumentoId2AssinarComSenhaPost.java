package br.jus.trf2.balcaovirtual;

import java.util.Random;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdDocumentoId2AssinarComSenhaPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdDocumentoId2AssinarComSenhaPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdDocumentoId2AssinarComSenhaPostResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostResponse;

public class MesaIdDocumentoId2AssinarComSenhaPost implements IMesaIdDocumentoId2AssinarComSenhaPost {

	@Override
	public void run(MesaIdDocumentoId2AssinarComSenhaPostRequest req,
			MesaIdDocumentoId2AssinarComSenhaPostResponse resp) throws Exception {
		if (!req.sistema.contains(".eproc"))
			throw new Exception("Operação disponível apenas para o Eproc");

		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (u.usuarios.get(req.sistema) == null)
			throw new PresentableUnloggedException("Login inválido para " + Utils.getName(req.sistema));

		if (!u.usuario.equalsIgnoreCase(req.username))
			throw new PresentableUnloggedException("Login não confere");

		if (!u.senha.equals(req.password)) {
			Random r = new Random();
			Thread.currentThread().sleep(2000 + r.nextInt(2000));
			throw new PresentableUnloggedException("Senha não confere");
		}

		UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostRequest q = new UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostRequest();
		Future<SwaggerAsyncResponse<UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostResponse>> future = SwaggerCall
				.callAsync(getContext(), Utils.getApiEprocPassword(req.sistema), "POST",
						Utils.getApiEprocUrl(req.sistema) + "/usuario/" + u.usuario + "/mesa/null/documento/" + req.id2
								+ "/assinar-com-senha",
						q, UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostResponse.class);
		SwaggerAsyncResponse<UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostResponse r = (UsuarioUsernameMesaIdDocumentoId2AssinarComSenhaPostResponse) sar
				.getResp();

		resp.status = r.status;
	}

	@Override
	public String getContext() {
		return "assinar minuta com senha";
	}

}

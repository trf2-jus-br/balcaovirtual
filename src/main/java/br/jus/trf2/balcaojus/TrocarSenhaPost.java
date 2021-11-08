package br.jus.trf2.balcaojus;

import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import javax.servlet.http.Cookie;

import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.ITrocarSenhaPost;
import br.jus.trf2.balcaojus.IBalcaojus.ListStatus;
import br.jus.trf2.balcaojus.util.AcessoPublico;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameTrocarSenhaPost;

@AcessoPublico
public class TrocarSenhaPost implements ITrocarSenhaPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {

		// Chama o método de autenticação
		AutenticarPost auth = new AutenticarPost();
		AutenticarPost.Request authReq = new AutenticarPost.Request();
		AutenticarPost.Response authResp = new AutenticarPost.Response();
		authReq.username = req.username;
		authReq.password = req.password;
		auth.run(authReq, authResp, ctx);

		// Read list from connected systems
		String[] systems = Utils.getSystems();
		if (systems == null)
			return;

		String authorization = "Basic " + SwaggerUtils.base64Encode((req.username + ":" + req.newpassword).getBytes());
		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (ListStatus authStatus : authResp.status) {
			String system = authStatus.system;
			if (authStatus.errormsg != null || !system.contains(".apolo"))
				continue;

			String urlsys = Utils.getApiUrl(system);

			IUsuarioUsernameTrocarSenhaPost.Request q = new IUsuarioUsernameTrocarSenhaPost.Request();
			q.username = req.username;
			mapp.put(system,
					new SwaggerCallParameters(system + "-trocar-senha-usuário", authorization, "POST",
							urlsys + "/usuario/" + req.username + "/trocar-senha", q,
							IUsuarioUsernameTrocarSenhaPost.Response.class));

		}

		
		
		/* Chama a troca de senha separadamente para evitar o bloqueio na sincronização das senhas*/
		SwaggerMultipleCallResult mcr = new SwaggerMultipleCallResult();
		Map<String, SwaggerCallParameters> mapAux = new HashMap<>();
		SwaggerMultipleCallResult mcrAux;
		for (String system : mapp.keySet()) {
			SwaggerCallParameters scp = mapp.get(system);
			mapAux.put(system,scp);
			mcrAux = SwaggerCall.callMultiple(mapAux, BalcaojusServlet.TIMEOUT_MILLISECONDS);
			mcr.responses.putAll(mcrAux.responses);
			mcr.status.addAll(mcrAux.status);
			
			mapAux.remove(system);
			
		}	
		resp.status = Utils.getStatus(mcr);
		
		//faz a autenticação com a nova senha para obter o token
		AutenticarPost auth2 = new AutenticarPost();
		AutenticarPost.Request authReq2 = new AutenticarPost.Request();
		AutenticarPost.Response authResp2 = new AutenticarPost.Response();
		authReq2.username = req.username;
		authReq2.password = req.newpassword;
		
		auth2.run(authReq2, authResp2, ctx);
		
		resp.id_token = authResp2.id_token;

	}

	@Override
	public String getContext() {
		return "trocar senha de usuário";
	}

}

package br.jus.trf2.balcaovirtual;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.INotificarPost;
import br.jus.trf2.balcaovirtual.util.AcessoPublico;

@AcessoPublico
public class NotificarPost implements INotificarPost {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		String auth = AutenticarPost.getAuthorizationHeader();
		String property = BalcaoVirtualServlet.INSTANCE.getProperty("notificar.password");
		if (property == null)
			throw new PresentableException(
					"Senha para habilitar notificações não foi configurada no parâmetro balcaovirtual.notificar.password");
		if (!property.equals(auth))
			throw new PresentableException(
					"Não foram informadas credenciais confiáveis para iniciar procedimento de notificação");
		Status.update(Notificar.NOTIFICACAO_STATUS, "Aguardando na fila de tarefas", 0, 100, 0L);
		BalcaoVirtualServlet.submitToExecutor(new Notificar());
	}

	@Override
	public String getContext() {
		return "gravar sinal";
	}

}

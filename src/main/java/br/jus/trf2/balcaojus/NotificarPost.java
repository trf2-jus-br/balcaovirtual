package br.jus.trf2.balcaojus;

import com.crivano.swaggerservlet.PresentableException;

import br.jus.trf2.balcaojus.IBalcaojus.INotificarPost;
import br.jus.trf2.balcaojus.util.AcessoPublico;

@AcessoPublico
public class NotificarPost implements INotificarPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		String auth = AutenticarPost.getAuthorizationHeader();
		String property = BalcaojusServlet.INSTANCE.getProperty("notificar.password");
		if (property == null)
			throw new PresentableException(
					"Senha para habilitar notificações não foi configurada no parâmetro balcaojus.notificar.password");
		if (!property.equals(auth))
			throw new PresentableException(
					"Não foram informadas credenciais confiáveis para iniciar procedimento de notificação");
		Status.update(Notificar.NOTIFICACAO_STATUS, "Aguardando na fila de tarefas", 0, 100, 0L);
		BalcaojusServlet.submitToExecutor(new Notificar());
	}

	@Override
	public String getContext() {
		return "gravar sinal";
	}

}

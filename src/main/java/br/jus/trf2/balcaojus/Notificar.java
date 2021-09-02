package br.jus.trf2.balcaojus;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.crivano.swaggerservlet.SwaggerUtils;

import br.jus.trf2.balcaojus.IBalcaojus.IProcessoNumeroValidarGet;
import br.jus.trf2.balcaojus.IBalcaojus.ProcessoValido;

public class Notificar implements Callable<String> {
	public final static String NOTIFICACAO_STATUS = "NOTIFICACAO_STATUS";
	private Status status;

	public Notificar() {
		super();
	}

	@Override
	public String call() throws Exception {
		try (Dao dao = new Dao()) {
			this.status = Status.update(NOTIFICACAO_STATUS, "Obtendo a lista de usuários para notificar", 0, 100, 0L);

			List<String> usuarios = dao.obtemUsuariosParaNotificar();
			if (usuarios == null) {
				this.status = Status.update(NOTIFICACAO_STATUS, "Nenhum usuário para ser notificado.", 100, 100, 0L);
				return null;
			}
			int i = 0;
			for (String usuario : usuarios) {
				i++;
				this.status = Status.update(NOTIFICACAO_STATUS,
						"Obtendo processos favoritos do usuário " + i + "/" + usuarios.size(), i, usuarios.size(), 0L);
				Map<String, Date> processos = dao.obtemProcessosDoUsuarioParaNotificar(usuario);
				if (processos == null)
					continue;

				this.status = Status.update(NOTIFICACAO_STATUS,
						"Obtendo data de última movimentação dos processos favoritos do usuário " + i + "/"
								+ usuarios.size(),
						i, usuarios.size(), 0L);
				IProcessoNumeroValidarGet.Response resp = new IProcessoNumeroValidarGet.Response();
				ProcessoNumeroValidarGet.validar(usuario,
						processos.keySet().toArray(new String[processos.keySet().size()]), resp);

				this.status = Status.update(NOTIFICACAO_STATUS,
						"Obtendo tokens de notificação do usuário " + i + "/" + usuarios.size(), i, usuarios.size(),
						0L);
				List<String> tokens = dao.obtemTokensDoUsuarioParaNotificar(usuario);

				this.status = Status.update(NOTIFICACAO_STATUS, "Notificando usuário " + i + "/" + usuarios.size(), i,
						usuarios.size(), 0L);
				notificarUsuario(dao, usuario, tokens, processos, resp.list);
			}
			this.status = Status.update(NOTIFICACAO_STATUS, "Notificação concluída", 100, 100, 0L);
		} catch (Exception ex) {
			this.status.ex = ex;
			Status.update(NOTIFICACAO_STATUS, this.status);
			SwaggerUtils.log(Notificar.class).error("Não foi possível enviar notificações", ex);
			Dao.rollbackCurrentTransaction();
		}
		return null;
	}

	private void notificarUsuario(Dao dao, String usuario, List<String> tokens, Map<String, Date> processos,
			List<ProcessoValido> list) throws Exception {
		// Calcular o body
		int c = 0;
		for (ProcessoValido pv : list) {
			Date dt = processos.get(pv.numero);
			if (dt == null || dt.before(pv.dataultimomovimento))
				c++;
		}
		if (c == 0)
			return;
		String body = usuario + " possui " + c
				+ (c > 1 ? " processos favoritos com movimentos novos." : " processo favorito com movimentos novos.");
		// SwaggerUtils.log(Notificar.class).info(body);

		String titulo = BalcaojusServlet.INSTANCE.getProperty("notificar.titulo");
		if (titulo != null) {
			String clickAction = BalcaojusServlet.INSTANCE.getProperty("notificar.url");
			String icon = BalcaojusServlet.INSTANCE.getProperty("base.url") + "/assets/icon-256x256.png";
			int sucessos = NotificarFirebase.enviarNotificacao(dao, tokens, titulo, body, clickAction, icon);
			// SwaggerUtils.log(Notificar.class)
			// .info(sucessos + (sucessos == 1 ? " notificação enviada." : " notificações
			// enviadas."));
		}
	}

}

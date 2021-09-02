package br.jus.trf2.balcaojus;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.INotificacaoIncluirTokenPost;
import br.jus.trf2.balcaojus.model.Notificacao;

public class NotificacaoIncluirTokenPost implements INotificacaoIncluirTokenPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		try (Dao dao = new Dao()) {
			Notificacao n = dao.obtemTokenParaNotificar(req.token);
			if (n != null) {
				if (n.getNotiCdUsu().equalsIgnoreCase(u.usuario)) {
					resp.status = "Previamente incluído";
					return;
				} else {
					n.setNotiCdUsu(u.usuario);
					dao.persist(n);
					resp.status = "OK";
					return;
				}
			} else {
				Notificacao n2 = new Notificacao();
				n2.setNotiCd(req.token);
				n2.setNotiCdUsu(u.usuario);
				dao.persist(n2);
				resp.status = "OK";
			}
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "incluir token de notificação";
	}

}

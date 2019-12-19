package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.INotificacaoIncluirTokenPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.NotificacaoIncluirTokenPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.NotificacaoIncluirTokenPostResponse;
import br.jus.trf2.balcaovirtual.model.Notificacao;

public class NotificacaoIncluirTokenPost implements INotificacaoIncluirTokenPost {

	@Override
	public void run(NotificacaoIncluirTokenPostRequest req, NotificacaoIncluirTokenPostResponse resp) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();

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

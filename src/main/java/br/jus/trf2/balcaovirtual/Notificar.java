package br.jus.trf2.balcaovirtual;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoValido;

public class Notificar implements Callable<String> {
	private final String NOTIFICACAO_STATUS = "NOTIFICACAO_STATUS";
	private Status status;

	public Notificar() {
		super();
	}

	@Override
	public String call() throws Exception {
		try {
			this.status = Status.update(NOTIFICACAO_STATUS, "Obtendo a lista de documentos", 0, 100, 0L);

			try (Dao dao = new Dao()) {
				List<String> usuarios = dao.obtemUsuariosParaNotificar();
				if (usuarios == null) {
					this.status = Status.update(NOTIFICACAO_STATUS, "Nenhum usuário para ser notificado.", 100, 100,
							0L);
					return null;
				}
				int i = 0;
				for (String usuario : usuarios) {
					i++;
					this.status = Status.update(NOTIFICACAO_STATUS,
							"Obtendo processos favoritos do usuário " + i + "/" + usuarios.size(), i, usuarios.size(),
							0L);
					Map<String, Date> processos = dao.obtemProcessosDoUsuarioParaNotificar(usuario);

					this.status = Status.update(NOTIFICACAO_STATUS,
							"Obtendo processos favoritos do usuário " + i + "/" + usuarios.size(), i, usuarios.size(),
							0L);

					ProcessoNumeroValidarGetResponse resp = new ProcessoNumeroValidarGetResponse();
					ProcessoNumeroValidarGet.validar(usuario,
							processos.keySet().toArray(new String[processos.keySet().size()]), resp);
					this.status = Status.update(NOTIFICACAO_STATUS, "Notificando usuário " + i + "/" + usuarios.size(),
							i, usuarios.size(), 0L);

					notificarUsuario(usuario, processos, resp.list);
				}
			}

			this.status = Status.update(NOTIFICACAO_STATUS, "Notificação concluída", 100, 100, 0L);
		} catch (Exception ex) {
			this.status.ex = ex;
			Status.update(NOTIFICACAO_STATUS, this.status);
		}
		return null;
	}

	private void notificarUsuario(String usuario, Map<String, Date> processos, List<ProcessoValido> list) {
		int c = 0;
		for (ProcessoValido pv : list) {
			Date dt = processos.get(pv);
			if (dt != null && dt.before(pv.dataultimomovimento))
				c++;
		}
		if (c == 0)
			return;
		System.out.println(usuario + " possui " + c + " processos favoritos com movimentos novos.");
	}

}

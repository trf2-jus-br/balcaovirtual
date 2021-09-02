package br.jus.trf2.balcaojus;

import br.jus.trf2.balcaojus.IBalcaojus.IStatusChaveGet;
import br.jus.trf2.balcaojus.util.AcessoPublico;

@AcessoPublico
public class StatusChaveGet implements IStatusChaveGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Status as = Status.get(req.chave);
		if (as == null)
			return;
		if (as.ex != null)
			throw as.ex;
		resp.mensagem = as.mensagem;
		resp.indice = (double) as.indice;
		resp.contador = (double) as.contador;
		if (as.bytes != null)
			resp.bytes = (double) as.bytes;
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

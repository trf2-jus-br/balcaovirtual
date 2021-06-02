package br.jus.trf2.balcaovirtual;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IStatusChaveGet;
import br.jus.trf2.balcaovirtual.util.AcessoPublico;

@AcessoPublico
public class StatusChaveGet implements IStatusChaveGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
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

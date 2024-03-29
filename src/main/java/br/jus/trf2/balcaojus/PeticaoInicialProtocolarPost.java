package br.jus.trf2.balcaojus;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IPeticaoInicialProtocolarPost;
import br.jus.trf2.balcaojus.SoapMNI.PeticaoInicial;

public class PeticaoInicialProtocolarPost implements IPeticaoInicialProtocolarPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		Gson gson = new GsonBuilder().create();
		Type type = new TypeToken<List<SoapMNI.Parte>>() {
		}.getType();

		List<SoapMNI.Parte> partes = gson.fromJson(req.partes, type);

		PeticaoInicial pi = SoapMNI.enviarPeticaoInicial(u.usuario, u.getSenha(), req.sistema, req.localidade,
				req.especialidade, req.classe, req.assunto, Utils.parsearValor(req.valorcausa), req.cdas, req.pas,
				Integer.parseInt(req.nivelsigilo), req.justicagratuita, req.tutelaantecipada, req.prioridadeidoso,
				partes, req.pdfs, req.classificacoes, req.nomepoloativo, req.nomepolopassivo);
		resp.status = pi.mensagem;
		resp.protocolo = pi.protocolo;
		resp.data = pi.data;
		resp.numero = pi.numProcFormatado;
		resp.unidade = pi.unidade;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

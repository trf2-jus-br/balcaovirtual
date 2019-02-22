package br.jus.trf2.balcaovirtual;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPeticaoInicialProtocolarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoInicialProtocolarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoInicialProtocolarPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.balcaovirtual.SoapMNI.PeticaoInicial;

public class PeticaoInicialProtocolarPost implements IPeticaoInicialProtocolarPost {

	@Override
	public void run(PeticaoInicialProtocolarPostRequest req, PeticaoInicialProtocolarPostResponse resp)
			throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();

		Gson gson = new GsonBuilder().create();
		Type type = new TypeToken<List<SoapMNI.Parte>>() {
		}.getType();

		List<SoapMNI.Parte> partes = gson.fromJson(req.partes, type);

		PeticaoInicial pi = SoapMNI.enviarPeticaoInicial(u.usuario, u.senha, req.sistema, req.localidade,
				req.especialidade, req.classe, Utils.parsearValor(req.valorcausa), req.cdas, req.pas,
				Integer.parseInt(req.nivelsigilo), req.justicagratuita, req.tutelaantecipada, req.prioridadeidoso,
				partes, req.pdfs, req.classificacoes, req.nomepoloativo, req.nomepolopassivo);
		resp.status = pi.mensagem;
		resp.protocolo = pi.protocolo;
		resp.data = pi.data;
		resp.numero = pi.numProcFormatado;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

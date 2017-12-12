package br.jus.trf2.balcaovirtual;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IPeticaoInicialProtocolarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoInicialProtocolarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.PeticaoInicialProtocolarPostResponse;

public class PeticaoInicialProtocolarPost implements IPeticaoInicialProtocolarPost {

	@Override
	public void run(PeticaoInicialProtocolarPostRequest req, PeticaoInicialProtocolarPostResponse resp)
			throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();

		Gson gson = new GsonBuilder().create();
		Type type = new TypeToken<List<SoapMNI.Parte>>() {
		}.getType();

		List<SoapMNI.Parte> partes = gson.fromJson(req.partes, type);

		String mensagem = SoapMNI.enviarPeticaoInicial(authorization, req.orgao, req.localidade, req.especialidade,
				req.classe, Utils.parsearValor(req.valorcausa), req.cdas, req.pas, Integer.parseInt(req.nivelsigilo),
				req.justicagratuita, req.tutelaantecipada, req.prioridadeidoso, partes, req.pdfs, req.classificacoes);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

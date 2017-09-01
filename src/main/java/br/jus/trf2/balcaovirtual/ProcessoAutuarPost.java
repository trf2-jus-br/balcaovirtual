package br.jus.trf2.balcaovirtual;

import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoAutuarPost;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoAutuarPostRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoAutuarPostResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetRequest;

public class ProcessoAutuarPost implements IProcessoAutuarPost {
	private static final Logger log = LoggerFactory.getLogger(ProcessoNumeroValidarGet.class);

	@Override
	public void run(ProcessoAutuarPostRequest req, ProcessoAutuarPostResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();

		Gson gson = new GsonBuilder().create();
		Type type = new TypeToken<List<SoapMNI.Parte>>() {
		}.getType();

		List<SoapMNI.Parte> partes = gson.fromJson(req.partes, type);

		String mensagem = SoapMNI.enviarPeticaoInicial(authorization, req.orgao, req.localidade, req.especialidade,
				Integer.parseInt(req.classe), Integer.parseInt(req.nivelsigilo), partes, req.pdfs, req.pdfs);
		resp.status = mensagem;
	}

	@Override
	public String getContext() {
		return "enviar petição intercorrente";
	}

}

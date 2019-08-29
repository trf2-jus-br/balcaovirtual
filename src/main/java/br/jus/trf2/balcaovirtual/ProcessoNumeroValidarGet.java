package br.jus.trf2.balcaovirtual;

import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.PresentableException;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IProcessoNumeroValidarGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroValidarGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.ProcessoValidarNumeroGetResponse;

public class ProcessoNumeroValidarGet implements IProcessoNumeroValidarGet {

	@Override
	public void run(ProcessoNumeroValidarGetRequest req, ProcessoNumeroValidarGetResponse resp) throws Exception {
		SessionsCreatePost.assertAuthorization();
		String url = null;
		try {
			Usuario u = SessionsCreatePost.assertUsuario();
			if (u.isInterno())
				url = "/usuario/" + u.usuario + "/processo/" + req.numero;
			else
				url = "/usuario-web/" + u.usuario + "/processo/" + req.numero;
		} catch (Exception e) {
			url = "/processo/validar/" + req.numero;
		}

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			ProcessoValidarNumeroGetRequest q = new ProcessoValidarNumeroGetRequest();
			q.numero = req.numero;
			mapp.put(system, new SwaggerCallParameters(system + " - validar número de processo", null, "GET",
					Utils.getApiUrl(system) + url, null, ProcessoValidarNumeroGetResponse.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, 15000);

		// TODO: Falta lógica para escolher o mais importante dos resultados.
		for (String system : mcr.responses.keySet()) {
			ProcessoValidarNumeroGetResponse r = (ProcessoValidarNumeroGetResponse) mcr.responses.get(system);
			if (r.numero == null || r.perdecompetencia)
				continue;
			if (resp.numero != null)
				throw new PresentableException("Não foi possível identificar qual sistema tem competência para o processo: " + resp.numero);
			resp.numero = r.numero;
			resp.sistema = system;
			resp.orgao = r.orgao;
			resp.unidade = r.unidade != null ? r.unidade.trim() : null;
			resp.localnaunidade = r.localNaUnidade;
			resp.segredodejustica = r.segredodejustica;
			resp.segredodejusticadesistema = r.segredodejusticadesistema;
			resp.segredodejusticaabsoluto = r.segredodejusticaabsoluto;
			resp.usuarioautorizado = r.usuarioautorizado;
			resp.digital = r.eletronico;
			resp.sentenciado = r.sentenciado;
			resp.baixado = r.baixado;
			resp.cdas = r.cdas;
			if (r.dataultimomovimento != null)
				resp.dataultimomovimento = Utils.parsearDataHoraMinuto(r.dataultimomovimento);
		}
	}

	@Override
	public String getContext() {
		return "validar número de processo";
	}

}

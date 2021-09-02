package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.Aviso;
import br.jus.trf2.balcaojus.IBalcaojus.IAvisoListarGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameAvisosGet;

public class AvisoListarGet implements IAvisoListarGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		List<String> mniSystems = new ArrayList<>();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			if (!u.usuarios.containsKey(system) || !"ext".equals(u.usuarios.get(system).origem))
				continue;
			if (system.contains(".eproc") && (req.mni == null || !req.mni)) {
				IUsuarioUsernameAvisosGet.Request q = new IUsuarioUsernameAvisosGet.Request();
				q.username = u.usuario;
				mapp.put(system,
						new SwaggerCallParameters(system + " - listar avisos", Utils.getApiPassword(system), "GET",
								Utils.getApiUrl(system) + "/usuario/" + u.usuario + "/avisos", q,
								IUsuarioUsernameAvisosGet.Response.class));

			} else {
				mniSystems.add(system);
			}
		}

		// Inicia a carga de avisos do Eproc
		SwaggerMultipleCallResult mcr = null;
		if (mapp.size() > 0) {
			mcr = SwaggerCall.callMultiple(mapp, 15000);
			resp.status = Utils.getStatus(mcr);
		}
		// Carrega avisos via MNI
		for (String system : mniSystems)
			SoapMNI.consultarAvisosPendentes(system, u.usuario, u.getSenha(), resp.list, resp.status);

		// Retoma a carga de avisos do Eproc
		if (mapp.size() > 0) {
			resp.status = Utils.getStatus(mcr);
			for (String system : mcr.responses.keySet()) {
				IUsuarioUsernameAvisosGet.Response r = (IUsuarioUsernameAvisosGet.Response) mcr.responses.get(system);
				for (ISistemaProcessual.Aviso a : r.list) {
					Aviso i = new Aviso();
					i.idaviso = a.idAviso;
					i.dataaviso = a.dataAviso;
					i.tipo = a.tipo;
					i.processo = a.processo;
					i.unidade = a.unidade;
					i.unidadenome = a.unidadeNome;
					i.unidadetipo = a.unidadeTipo;
					i.orgao = a.orgao;
					i.sistema = system;
					i.localidade = a.localidade;
					i.teor = a.teor;
					i.eventointimacao = a.eventoIntimacao;
					i.motivointimacao = a.motivoIntimacao;
					i.numeroprazo = a.numeroPrazo;
					i.tipoprazo = a.tipoPrazo;
					i.multiplicadorprazo = a.multiplicadorPrazo;
					i.datalimiteintimacaoautomatica = a.dataLimiteIntimacaoAutomatica;
					i.datafinalprazo = a.dataFinalPrazo;
					i.assunto = a.assunto;
					resp.list.add(i);
				}
			}
		}
	}

	@Override
	public String getContext() {
		return "consultar avisos pendentes";
	}

}

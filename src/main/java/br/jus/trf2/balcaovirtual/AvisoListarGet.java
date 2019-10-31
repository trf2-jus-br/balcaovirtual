package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Aviso;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoListarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoListarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoListarGet;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameAvisosGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameAvisosGetResponse;

public class AvisoListarGet implements IAvisoListarGet {

	@Override
	public void run(AvisoListarGetRequest req, AvisoListarGetResponse resp) throws Exception {
		Usuario u = AutenticarPost.assertUsuario();

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		List<String> mniSystems = new ArrayList<>();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			if (!u.usuarios.containsKey(system) || !"ext".equals(u.usuarios.get(system).origem))
				continue;
			if (system.contains(".eproc")) {
				UsuarioUsernameAvisosGetRequest q = new UsuarioUsernameAvisosGetRequest();
				q.username = u.usuario;
				mapp.put(system,
						new SwaggerCallParameters(system + " - listar avisos", Utils.getApiPassword(system), "GET",
								Utils.getApiUrl(system) + "/usuario/" + u.usuario + "/avisos", q,
								UsuarioUsernameAvisosGetResponse.class));

			} else {
				mniSystems.add(system);
			}
		}

		// Inicia a carga de avisos do Eproc
		SwaggerMultipleCallResult mcr = null;
		if (mapp.size() > 0)
			mcr = SwaggerCall.callMultiple(mapp, 15000);

		// Carrega avisos via MNI
		for (String system : mniSystems)
			SoapMNI.consultarAvisosPendentes(system, u.usuario, u.senha, resp.list, resp.status);

		// Retoma a carga de avisos do Eproc
		if (mapp.size() > 0) {
			resp.status = Utils.getStatus(mcr);
			for (String system : mcr.responses.keySet()) {
				UsuarioUsernameAvisosGetResponse r = (UsuarioUsernameAvisosGetResponse) mcr.responses.get(system);
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

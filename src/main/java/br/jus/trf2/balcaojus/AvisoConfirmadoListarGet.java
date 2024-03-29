package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaojus.IBalcaojus.Aviso;
import br.jus.trf2.balcaojus.IBalcaojus.IAvisoConfirmadoListarGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameAvisoConfirmadoListarGet;

public class AvisoConfirmadoListarGet implements IAvisoConfirmadoListarGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Map<String, Object> jwt = AutenticarPost.assertUsuarioAutorizado();

		Date dtFim = Utils.parsearData(req.datafinal);
		Date dtFimPlus1 = new Date(dtFim.getTime() + (1000 * 60 * 60 * 24));
		String datafinal = Utils.formatarData(dtFimPlus1);

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			mapp.put(system,
					new SwaggerCallParameters(system + " - obter quantidade de avisos confirmado",
							Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + jwt.get("username")
									+ "/aviso-confirmado/listar?dataInicial=" + req.datainicial + "&dataFinal="
									+ datafinal + "&confirmacao=" + req.confirmacao.toString() + "&omissao="
									+ req.omissao.toString() + "&grupo=" + req.grupo.toString(),
							null, IUsuarioUsernameAvisoConfirmadoListarGet.Response.class));

		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, BalcaojusServlet.TIMEOUT_MILLISECONDS);
		resp.status = Utils.getStatus(mcr);

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		for (String system : mcr.responses.keySet()) {
			IUsuarioUsernameAvisoConfirmadoListarGet.Response r = (IUsuarioUsernameAvisoConfirmadoListarGet.Response) mcr.responses
					.get(system);
			if (r.list != null)
				for (br.jus.trf2.sistemaprocessual.ISistemaProcessual.Aviso ra : r.list) {
					Aviso a = new Aviso();
					a.idaviso = ra.idAviso;
					a.dataaviso = ra.dataAviso;
					a.tipo = ra.tipo;
					a.processo = ra.processo;
					a.unidade = ra.unidade;
					a.unidadetipo = ra.unidadeTipo;
					a.sistema = system;
					a.localidade = ra.localidade;
					a.eventointimacao = ra.eventoIntimacao;
					a.motivointimacao = ra.motivoIntimacao;
					a.numeroprazo = ra.numeroPrazo;
					a.tipoprazo = ra.tipoPrazo;
					a.multiplicadorprazo = ra.multiplicadorPrazo;
					a.datalimiteintimacaoautomatica = ra.dataLimiteIntimacaoAutomatica;
					a.assunto = ra.assunto;
					a.dataconfirmacao = Utils.parsearDataHoraMinutoSegundo(ra.dataConfirmacao);
					a.usuarioconfirmacao = ra.usuarioConfirmacao;
					a.teor = ra.teor;
					resp.list.add(a);
				}
		}
	}

	@Override
	public String getContext() {
		return "consultar avisos confirmados";
	}

}

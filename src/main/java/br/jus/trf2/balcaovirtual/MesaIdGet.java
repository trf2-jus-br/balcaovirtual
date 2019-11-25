package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaDocumento;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetResponse;
import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameLocalIdMesaId2DocumentosGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameLocalIdMesaId2DocumentosGetResponse;

public class MesaIdGet implements IMesaIdGet {

	@Override
	public void run(MesaIdGetRequest req, MesaIdGetResponse resp) throws Exception {
		Usuario u = AutenticarPost.assertUsuario();
		if (!u.isInterno())
			throw new Exception("Mesas só podem ser acessadas por usuários internos");

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			if (!u.usuarios.containsKey(system) || !"int".equals(u.usuarios.get(system).origem)
					|| !system.contains(".eproc"))
				continue;

			UsuarioUsernameLocalIdMesaId2DocumentosGetRequest q = new UsuarioUsernameLocalIdMesaId2DocumentosGetRequest();
			q.username = u.usuario;
			mapp.put(system,
					new SwaggerCallParameters(system + " - listar minutas", Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + u.usuario + "/local/null/mesa/null/documentos", q,
							UsuarioUsernameLocalIdMesaId2DocumentosGetResponse.class));
		}

		SwaggerMultipleCallResult mcr = null;
		mcr = SwaggerCall.callMultiple(mapp, 15000);
		resp.status = Utils.getStatus(mcr);
		for (String system : mcr.responses.keySet()) {
			UsuarioUsernameLocalIdMesaId2DocumentosGetResponse r = (UsuarioUsernameLocalIdMesaId2DocumentosGetResponse) mcr.responses
					.get(system);
			for (ISistemaProcessual.MesaDocumento a : r.list) {
				IBalcaoVirtual.MesaDocumento i = new MesaDocumento();
				i.dataDeInclusao = a.dataDeInclusao;
				i.id = a.id;
				i.numeroDoProcesso = a.numeroDoProcesso;
				i.autor = Texto.maiusculasEMinusculas(a.autor);
				i.reu = Texto.maiusculasEMinusculas(a.reu);
				i.numeroDoDocumento = a.numeroDoDocumento;
				i.descricao = a.descricao;
				i.status = a.status;
				i.descricaoDoStatus = a.descricaoDoStatus;
				i.tipoDoDocumento = Texto.maiusculasEMinusculas(a.tipoDoDocumento);
				i.identificadorDoUsuarioQueIncluiu = a.identificadorDoUsuarioQueIncluiu;
				i.nomeDoUsuarioQueIncluiu = a.nomeDoUsuarioQueIncluiu;
				i.siglaDaUnidade = a.siglaDaUnidade;
				i.conteudo = a.conteudo;
				i.sistema = system;
				if (a.lembretes != null) {
					i.lembretes = new ArrayList<>();
					for (ISistemaProcessual.Lembrete j : a.lembretes) {
						IBalcaoVirtual.Lembrete lembrete = new IBalcaoVirtual.Lembrete();
						lembrete.id = j.id;
						lembrete.dataDeInclusao = j.dataDeInclusao;
						lembrete.conteudo = j.conteudo;
						lembrete.identificadorDoUsuario = j.identificadorDoUsuario;
						lembrete.nomeDoUsuario = j.nomeDoUsuario;
						i.lembretes.add(lembrete);
					}
				}
				resp.list.add(i);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter lista de minutas";
	}

}

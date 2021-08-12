package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaovirtual.AutenticarPost.Usuario;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IVotosGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Voto;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameVotosGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.VotoDocumento;

public class VotosGet implements IVotosGet {

	@Override
	public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception {
		Usuario u = BalcaoVirtualServlet.getPrincipal();
		if (!u.isInterno())
			throw new Exception("Votos só podem ser acessadas por usuários internos");

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			if (!u.usuarios.containsKey(system) || !"int".equals(u.usuarios.get(system).origem)
					|| !system.contains(".eproc"))
				continue;

			IUsuarioUsernameVotosGet.Request q = new IUsuarioUsernameVotosGet.Request();
			q.username = u.usuario;
			mapp.put(system,
					new SwaggerCallParameters(system + " - listar votos", Utils.getApiPassword(system), "GET",
							Utils.getApiEprocVotosUrl(system) + "/sessao_julgamento?sigla=" + u.usuario, q,
							IUsuarioUsernameVotosGet.Response.class));
		}

		SwaggerMultipleCallResult mcr = null;
		mcr = SwaggerCall.callMultiple(mapp, 15000);
		resp.status = Utils.getStatus(mcr);
		for (String system : mcr.responses.keySet()) {
			IUsuarioUsernameVotosGet.Response r = (IUsuarioUsernameVotosGet.Response) mcr.responses.get(system);
			for (ISistemaProcessual.VotoDocumento a : r.list) {
				IBalcaoVirtual.Voto i = new Voto();
				i.dataDeInclusao = Utils.parsearData(a.dataDeInclusao);
				i.id = a.id;
				i.numeroDoProcesso = a.numeroDoProcesso;
				i.autor = Texto.maiusculasEMinusculas(a.autor);
				i.reu = Texto.maiusculasEMinusculas(a.reu);
				i.relator = Texto.abreviarNome(Texto.maiusculasEMinusculas(a.relator));
				i.numeroDoDocumento = a.numeroDoDocumento;
				i.status = a.comSituacaoProcesso;
//				i.descricao = a.descricao;
//				i.descricaoDoStatus = a.descricaoDoStatus;
//				i.tipoDoDocumento = Texto.maiusculasEMinusculas(a.tipoDoDocumento);
//				i.identificadorDoUsuarioQueIncluiu = a.identificadorDoUsuarioQueIncluiu;
//				i.nomeDoUsuarioQueIncluiu = a.nomeDoUsuarioQueIncluiu;
				i.siglaDaUnidade = a.siglaDaUnidade;
				i.conteudo = a.conteudo;
				i.sistema = system;

				if (a.voto != null) {
					int va = 0, vd = 0, vp = 0;
					i.votosProferidos = new ArrayList<>();
					for (ISistemaProcessual.VotoProferidoItem j : a.voto) {
						IBalcaoVirtual.VotoProferido v = new IBalcaoVirtual.VotoProferido();
						v.dataDeInclusao = Utils.parsearDataHoraMinutoSegundo(j.dataDeInclusao);
						v.magistrado = Texto.abreviarNome(Texto.maiusculasEMinusculas(j.magistrado));
						v.voto = j.voto;
						i.votosProferidos.add(v);

						if (v.voto == null)
							continue;
						if (v.voto.contains("Relator"))
							va++;
						else if (v.voto.contains("Divergência"))
							vd++;
						else if (v.voto.contains("Vista"))
							vp++;
					}
//					if (va > 0)
						i.acompanhamentos = Integer.toString(va);
//					if (vd > 0)
						i.divergencias = Integer.toString(vd);
//					if (vp > 0)
						i.pedidosDeVista = Integer.toString(vp);
				}

				resp.list.add(i);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter lista de votos";
	}

}

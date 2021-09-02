package br.jus.trf2.balcaojus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IMesaIdGet;
import br.jus.trf2.balcaojus.IBalcaojus.MesaDocumento;
import br.jus.trf2.balcaojus.model.Padrao;
import br.jus.trf2.balcaojus.util.Markdown;
import br.jus.trf2.balcaojus.util.PadraoUtils.Minuta;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.IUsuarioUsernameLocalIdMesaId2DocumentosGet;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

public class MesaIdGet implements IMesaIdGet {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();
		if (!u.isInterno())
			throw new Exception("Mesas só podem ser acessadas por usuários internos");

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : Utils.getSystems()) {
			if (!u.usuarios.containsKey(system) || !"int".equals(u.usuarios.get(system).origem)
					|| !system.contains(".eproc"))
				continue;

			IUsuarioUsernameLocalIdMesaId2DocumentosGet.Request q = new IUsuarioUsernameLocalIdMesaId2DocumentosGet.Request();
			q.username = u.usuario;
			mapp.put(system,
					new SwaggerCallParameters(system + " - listar minutas", Utils.getApiPassword(system), "GET",
							Utils.getApiUrl(system) + "/usuario/" + u.usuario + "/local/null/mesa/null/documentos", q,
							IUsuarioUsernameLocalIdMesaId2DocumentosGet.Response.class));
		}

		SwaggerMultipleCallResult mcr = null;
		mcr = SwaggerCall.callMultiple(mapp, 15000);
		resp.status = Utils.getStatus(mcr);
		for (String system : mcr.responses.keySet()) {
			IUsuarioUsernameLocalIdMesaId2DocumentosGet.Response r = (IUsuarioUsernameLocalIdMesaId2DocumentosGet.Response) mcr.responses
					.get(system);
			for (ISistemaProcessual.MesaDocumento a : r.list) {
				IBalcaojus.MesaDocumento i = new MesaDocumento();
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
						IBalcaojus.Lembrete lembrete = new IBalcaojus.Lembrete();
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

		// Pipeline para minuta-padrão
		if (resp.list.size() == 0)
			return;

		// Carregar padrões
		List<Minuta> padroes = new ArrayList<>();
		try (Dao dao = new Dao()) {
			List<Padrao> l = dao.obtemPadroes(u.usuario);
			for (Padrao i : l) {
				padroes.add(new Minuta(i.getPadrId().toString(), i.getPadrTxConteudo()));
			}
		}
		if (padroes.size() == 0)
			return;

		// Carregar Minutas
		List<Minuta> minutas = new ArrayList<>();
		for (MesaDocumento m : resp.list) {
			if (m.conteudo == null || m.conteudo.trim().isEmpty())
				continue;
			minutas.add(new Minuta(m));
		}

		// Processar similaridade
		NormalizedStringSimilarity metric = new SorensenDice();
		for (Minuta minuta : minutas) {
			for (Minuta padrao : padroes) {
				double coef = 0d;
				if (minuta.markdown.equals(padrao.markdown))
					coef = 1d;
				else
					coef = 0.99d * metric.similarity(minuta.markdownSimplificado, padrao.markdownSimplificado);

				if (coef > 0.75d) {
					if (minuta.similaridade == 0d || minuta.similaridade < coef) {
						minuta.padrao = padrao;
						minuta.similaridade = coef;
					}
				}
			}
		}

		for (Minuta minuta : minutas) {
			if (minuta.padrao == null)
				continue;
			minuta.calcularDiff();
			minuta.doc.similaridade = minuta.similaridade;
			minuta.doc.diferencas = minuta.htmlDiff;
			minuta.doc.idPadrao = minuta.padrao.id;
			System.out.println(minuta.markdownDiff);
			System.out.println(minuta.similaridade + " -> " + minuta.markdownSimplificado);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de minutas";
	}

}

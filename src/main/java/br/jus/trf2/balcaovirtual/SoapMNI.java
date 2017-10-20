package br.jus.trf2.balcaovirtual;

import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.crivano.swaggerservlet.SwaggerUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import br.jus.cnj.intercomunicacao_2_2.ModalidadePoloProcessual;
import br.jus.cnj.intercomunicacao_2_2.ModalidadeRelacionamentoProcessual;
import br.jus.cnj.intercomunicacao_2_2.TipoAvisoComunicacaoPendente;
import br.jus.cnj.intercomunicacao_2_2.TipoCabecalhoProcesso;
import br.jus.cnj.intercomunicacao_2_2.TipoComunicacaoProcessual;
import br.jus.cnj.intercomunicacao_2_2.TipoDocumento;
import br.jus.cnj.intercomunicacao_2_2.TipoParametro;
import br.jus.cnj.intercomunicacao_2_2.TipoParte;
import br.jus.cnj.intercomunicacao_2_2.TipoPessoa;
import br.jus.cnj.intercomunicacao_2_2.TipoPoloProcessual;
import br.jus.cnj.intercomunicacao_2_2.TipoProcessoJudicial;
import br.jus.cnj.intercomunicacao_2_2.TipoQualificacaoPessoa;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222_Service;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Aviso;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ListStatus;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroAvisoIdReceberPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

public class SoapMNI {
	private static final DateTimeFormatter dtfMNI = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	private static final DateTimeFormatter dtfBR = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
	// private static final DateTimeFormatter dtfFILE =
	// DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");

	private static class ConsultaProcessualExclStrat implements ExclusionStrategy {

		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {

			return f.getName().equals("endereco");
		}

	}

	private static class OutroParametroSerializer implements JsonSerializer<List<TipoParametro>> {
		@Override
		public JsonElement serialize(List<TipoParametro> src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject object = new JsonObject();

			for (TipoParametro p : src) {
				String nome = p.getNome();
				String valor = p.getValor();

				if (object.has(nome)) {
					if (object.get(nome).isJsonArray()) {
						object.getAsJsonArray(nome).add(valor);
					} else {
						JsonArray a = new JsonArray();
						a.add(object.get(nome).getAsString());
						a.add(valor);
						object.add(nome, a);
					}
				} else
					object.addProperty(nome, valor);
			}

			return object;
		}
	}

	public static String consultarProcesso(String idManif, String orgao, String numProc) throws Exception {
		URL url = new URL(Utils.getMniWsdlUrl(orgao));
		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();

		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<TipoProcessoJudicial> processo = new Holder<>();

		Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
		requestContext.put("javax.xml.ws.client.receiveTimeout", "3600000");
		requestContext.put("javax.xml.ws.client.connectionTimeout", "5000");

		client.consultarProcesso(idManif, null, numProc, null, true, true, true, null, sucesso, mensagem, processo);
		if (!sucesso.value)
			throw new Exception(mensagem.value);

		Type collectionType = new TypeToken<List<TipoParametro>>() {
		}.getType();
		Gson gson = new GsonBuilder().registerTypeAdapter(collectionType, new OutroParametroSerializer())
				.setExclusionStrategies(new ConsultaProcessualExclStrat()).create();
		return gson.toJson(processo);
	}

	public static byte[] obterPecaProcessual(String idManif, String orgao, String numProc, String documento)
			throws Exception {
		URL url = new URL(Utils.getMniWsdlUrl(orgao));
		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<TipoProcessoJudicial> processo = new Holder<>();
		List<String> l = new ArrayList<>();
		l.add(documento);

		client.consultarProcesso(idManif, null, numProc, null, false, false, false, l, sucesso, mensagem, processo);
		if (!sucesso.value)
			throw new Exception(mensagem.value);
		return processo.value.getDocumento().get(0).getConteudo();
	}

	public static void consultarAvisosPendentes(String idConsultante, List<Aviso> list, List<ListStatus> status)
			throws Exception {

		Usuario u = SessionsCreatePost.assertUsuario();

		for (String orgao : Utils.getOrgaos().split(",")) {
			String system = orgao.toLowerCase();
			if (!u.usuarios.containsKey(system))
				continue;
			URL url = new URL(Utils.getMniWsdlUrl(system));
			ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
			ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
			Holder<Boolean> sucesso = new Holder<>();
			Holder<String> mensagem = new Holder<>();
			Holder<List<TipoAvisoComunicacaoPendente>> aviso = new Holder<>();

			ListStatus ls = new ListStatus();
			ls.system = system;
			status.add(ls);
			try {
				client.consultarAvisosPendentes(null, idConsultante, null, null, sucesso, mensagem, aviso);
				if (!sucesso.value)
					throw new Exception(mensagem.value);
			} catch (Exception ex) {
				SwaggerUtils.log(SoapMNI.class).error("Erro obtendo a lista de {}", system, ex);
				ls.errormsg = SwaggerUtils.messageAsString(ex);
				ls.stacktrace = SwaggerUtils.stackAsString(ex);
			}

			if (aviso != null && aviso.value != null) {
				for (TipoAvisoComunicacaoPendente a : aviso.value) {
					Aviso i = new Aviso();
					switch (a.getTipoComunicacao()) {
					case "INT":
						i.tipo = "Intimação";
						break;
					case "CIT":
						i.tipo = "Citação";
						break;
					}
					i.processo = a.getProcesso().getNumero();
					i.dataaviso = a.getDataDisponibilizacao();
					i.idaviso = a.getIdAviso();
					i.orgao = orgao;
					i.unidade = a.getProcesso().getOrgaoJulgador().getCodigoOrgao();
					i.unidadenome = a.getProcesso().getOrgaoJulgador().getNomeOrgao();
					if (a.getProcesso().getAssunto() != null && a.getProcesso().getAssunto().size() > 0
							&& a.getProcesso().getAssunto().get(0) != null
							&& a.getProcesso().getAssunto().get(0).getCodigoNacional() != null)
						i.assunto = a.getProcesso().getAssunto().get(0).getCodigoNacional().toString();
					for (TipoParametro p : a.getProcesso().getOutroParametro()) {
						if (p.getNome().equals("tipoOrgaoJulgador"))
							i.unidadetipo = p.getValor();
						if (p.getNome().equals("dtLimitIntimAut"))
							i.datalimiteintimacaoautomatica = p.getValor();
						if (p.getNome().equals("eventoIntimacao"))
							i.eventointimacao = p.getValor();
						if (p.getNome().equals("numeroPrazo"))
							i.numeroprazo = p.getValor();
						if (p.getNome().equals("tipoPrazo"))
							i.tipoprazo = p.getValor();
						if (p.getNome().equals("multiplicadorPrazo"))
							i.multiplicadorprazo = p.getValor();
						if (p.getNome().equals("motivoIntimacao"))
							i.motivointimacao = p.getValor();
					}
					i.localidade = a.getProcesso().getCodigoLocalidade();
					list.add(i);
				}
			}
		}
	}

	public static void consultarTeorComunicacao(String idConsultante, String numProc, String idAviso, String orgao,
			ProcessoNumeroAvisoIdReceberPostResponse resp) throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();
		String email = (String) jwt.get("email");
		String nome = (String) jwt.get("name");
		String usuario = (String) jwt.get("username");

		String numProcFormated = numProc;
		try {
			numProcFormated = numProc.replaceAll("^(\\d{7})-?(\\d{2})\\.?(\\d{4})\\.?(4)\\.?(02)\\.?(\\d{4})(\\d{2})?",
					"$1-$2.$3.$4.$5.$6$7");
		} catch (Exception ex) {
		}

		String system = orgao.toLowerCase();
		URL url = new URL(Utils.getMniWsdlUrl(system));
		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<List<TipoComunicacaoProcessual>> comunicacao = new Holder<>();

		client.consultarTeorComunicacao(idConsultante, idAviso, null, null, sucesso, mensagem, comunicacao);
		if (!sucesso.value)
			throw new Exception(mensagem.value);

		if (comunicacao.value.size() != 1)
			throw new Exception("Número de comunicações deve ser exatamente igual a 1");

		TipoComunicacaoProcessual c = comunicacao.value.get(0);
		if (c.getTipoComunicacao() != null)
			switch (c.getTipoComunicacao()) {
			case "INT":
				resp.tipo = "Intimação";
				break;
			case "CIT":
				resp.tipo = "Citação";
				break;
			default:
				resp.tipo = "Aviso";
			}
		resp.processo = numProc;
		resp.dataaviso = c.getDataReferencia();
		resp.idaviso = idAviso;
		resp.orgao = orgao;
		resp.teor = c.getTeor();

		// byte[] pdf = null;
		// if (c.getDocumento() != null && c.getDocumento().size() > 0)
		// pdf = c.getDocumento().get(0).getConteudo();

		DateTime dt = DateTime.parse(c.getDataReferencia(), dtfMNI);

		boolean sent = false;
		boolean sigilo = c.getNivelSigilo() != null ? c.getNivelSigilo() != 0 : true;
		if (email != null) {
			email = "renato.crivano@gmail.com";
			try {
				String assunto = "Balcão Virtual: Confirmação de " + resp.tipo;
				String conteudo = "Prezado(a) " + nome + ",\n\nAcusamos a confirmação de " + resp.tipo.toLowerCase()
						+ " conforme dados abaixo:" + "\n\nProcesso Número: " + numProcFormated.replace("/", "")
						+ "\nData/Hora de Término do Prazo: " + dt.toString(dtfBR) + "\nSigilo: "
						+ (sigilo ? "Sim" : "Não") + "\n\nAtenciosamente,\n\nTribunal Regional Federal da 2a Região";
				// String nomeArquivo = numProcFormated + "-" +
				// Utils.removeAcento(resp.tipo).toLowerCase() + "-"
				// + dt.toString(dtfFILE) + ".pdf";
				// if (sigilo)
				Correio.enviar(email, assunto, conteudo, null, null, null);
				// else
				// Correio.enviar(email, assunto, conteudo, nomeArquivo,
				// "application/pdf", pdf);
				sent = true;
			} catch (Exception ex) {
				SwaggerUtils.log(SoapMNI.class).error("Email não enviado", ex);
			}
		}

		SwaggerUtils.log(SoapMNI.class).warn("*** Processo: " + numProcFormated + " Aviso confirmado: " + resp.idaviso
				+ " Por: " + usuario + " Email: " + email + (sent ? "" : " (email não enviado)"));

	}

	public static String enviarPeticaoIntercorrente(String idManif, String orgao, String numProc, String tpDoc,
			int nvlSigilo, String nomePdfs, byte pdf[]) throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();
		String email = (String) jwt.get("email");
		String nome = (String) jwt.get("name");
		String usuario = (String) jwt.get("username");

		if (nomePdfs == null && pdf == null)
			throw new Exception("Não é possível peticionar sem que seja fornecido um PDF");

		String numProcFormated = numProc;
		try {
			numProcFormated = numProc.replaceAll("^(\\d{7})-?(\\d{2})\\.?(\\d{4})\\.?(4)\\.?(02)\\.?(\\d{4})(\\d{2})?",
					"$1-$2.$3.$4.$5.$6$7");
		} catch (Exception ex) {
		}

		String dataEnvio = new DateTime(new Date()).toString("yyyyMMddHHmmss");
		String dirFinal = Utils.getDirFinal();
		URL url = new URL(Utils.getMniWsdlUrl(orgao));
		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
		List<TipoDocumento> l = new ArrayList<>();
		if (nomePdfs != null) {
			for (String nomePdf : nomePdfs.split(",")) {
				TipoDocumento doc = new TipoDocumento();
				doc.setMimetype("application/pdf");
				doc.setDataHora(dataEnvio);
				doc.setNivelSigilo(nvlSigilo);
				doc.setTipoDocumento(tpDoc);
				Path path = Paths.get(dirFinal + "/" + nomePdf + ".pdf");
				byte[] data = Files.readAllBytes(path);
				doc.setConteudo(data);
				l.add(doc);
			}
		}
		if (pdf != null) {
			TipoDocumento doc = new TipoDocumento();
			doc.setMimetype("application/pdf");
			doc.setDataHora(dataEnvio);
			doc.setNivelSigilo(0);
			doc.setTipoDocumento(tpDoc);
			doc.setConteudo(pdf);
			l.add(doc);
		}

		TipoCabecalhoProcesso dadosBasicos = new TipoCabecalhoProcesso();
		dadosBasicos.setCodigoLocalidade("1");
		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<String> protocoloRecebimento = new Holder<>();
		Holder<String> dataOperacao = new Holder<>();
		Holder<byte[]> recibo = new Holder<>();
		Holder<List<TipoParametro>> parametro = new Holder<>();

		client.entregarManifestacaoProcessual(idManif, null, numProc, null, l, dataEnvio,
				new ArrayList<TipoParametro>(), sucesso, mensagem, protocoloRecebimento, dataOperacao, recibo,
				parametro);
		if (!sucesso.value)
			throw new Exception(mensagem.value);

		DateTime dt = DateTime.parse(dataOperacao.value, dtfMNI);

		boolean sent = false;
		if (email != null) {
			try {
				String conteudo = "Prezado(a) " + nome
						+ ",\n\nAcusamos o recebimento da petição intercorrente conforme dados abaixo:"
						+ "\n\nProcesso Número: " + numProcFormated + "\nProtocolo: " + protocoloRecebimento.value
						+ "\nData/Hora do Protocolo: " + dt.toString(dtfBR)
						+ "\n\nAtenciosamente,\n\nTribunal Regional Federal da 2a Região";
				Correio.enviar(email, "Balcão Virtual: Protocolo de Recebimento", conteudo,
						numProcFormated + "-protocolo-" + protocoloRecebimento.value + ".pdf", "application/pdf",
						recibo.value);
				sent = true;
			} catch (Exception ex) {
				SwaggerUtils.log(SoapMNI.class).error("Email não enviado", ex);
			}
		}

		SwaggerUtils.log(SoapMNI.class)
				.warn("*** Processo: " + numProcFormated + " Petição Intercorrente protocolada: "
						+ protocoloRecebimento.value + " Por: " + usuario + " Email: " + email
						+ (sent ? "" : " (email não enviado)"));

		return "Protocolo: " + protocoloRecebimento.value + ", Data: " + dt.toString(dtfBR)
				+ (sent ? "" : " (email não enviado)");
	}

	public static class Parte {
		int polo; // 1=Ativo, 2=Passivo
		int tipopessoa; // 1=PF, 2=PJ, 3=Entidade, 4=Advogado
		String documento;
		String nome;
	}

	public static String enviarPeticaoInicial(String idManif, String orgao, String localidade, String especialidade,
			int classe, String cdas, String pas, int nvlSigilo, List<Parte> partes, String nomePdfs, String tpDocPdfs)
			throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();
		String email = (String) jwt.get("email");
		String nome = (String) jwt.get("name");
		String usuario = (String) jwt.get("username");

		String dataEnvio = new DateTime(new Date()).toString("yyyyMMddHHmmss");
		String dirFinal = Utils.getDirFinal();
		URL url = new URL(Utils.getMniWsdlUrl(orgao));
		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
		List<TipoDocumento> l = new ArrayList<>();
		// String tpDocs[] = tpDocPdfs.split(",");
		int i = 0;
		String classificacoes[] = tpDocPdfs.split(",");
		for (String nomePdf : nomePdfs.split(",")) {
			TipoDocumento doc = new TipoDocumento();
			doc.setMimetype("application/pdf");
			doc.setDataHora(dataEnvio);
			doc.setNivelSigilo(nvlSigilo);
			// doc.setTipoDocumento(tpDocs[i]);
			Path path = Paths.get(dirFinal + "/" + nomePdf + ".pdf");
			byte[] data = Files.readAllBytes(path);
			doc.setConteudo(data);
			TipoParametro classificacao = new TipoParametro();
			classificacao.setNome("classificacao");
			classificacao.setValor(classificacoes[i]);
			doc.getOutroParametro().add(classificacao);
			l.add(doc);
			i++;
		}

		TipoCabecalhoProcesso dadosBasicos = new TipoCabecalhoProcesso();
		for (Parte parte : partes) {
			ModalidadePoloProcessual m = parte.polo == 1 ? ModalidadePoloProcessual.AT : ModalidadePoloProcessual.PA;
			TipoPoloProcessual tpp = null;
			for (TipoPoloProcessual itpp : dadosBasicos.getPolo()) {
				if (itpp.getPolo().equals(m)) {
					tpp = itpp;
					break;
				}
			}
			if (tpp == null) {
				tpp = new TipoPoloProcessual();
				tpp.setPolo(m);
				dadosBasicos.getPolo().add(tpp);
			}

			TipoQualificacaoPessoa tqp = null;
			switch (parte.tipopessoa) {
			case 1:
				tqp = TipoQualificacaoPessoa.FISICA;
				break;
			case 2:
				tqp = TipoQualificacaoPessoa.JURIDICA;
				break;
			case 3:
				tqp = TipoQualificacaoPessoa.AUTORIDADE;
				break;
			case 4:
				tqp = TipoQualificacaoPessoa.ORGAOREPRESENTACAO;
				break;
			}

			TipoParte tp = new TipoParte();
			tp.setRelacionamentoProcessual(ModalidadeRelacionamentoProcessual.RP);
			TipoPessoa pess = new TipoPessoa();
			pess.setNome(parte.nome);
			pess.setNumeroDocumentoPrincipal(Utils.removePontuacao(parte.documento));
			pess.setTipoPessoa(tqp);
			tp.setPessoa(pess);
			tpp.getParte().add(tp);
		}

		dadosBasicos.setCodigoLocalidade(localidade);
		dadosBasicos.setClasseProcessual(classe);
		ArrayList<TipoParametro> parametros = new ArrayList<TipoParametro>();

		if (cdas != null) {
			for (String s : cdas.split(",")) {
				String ss = Utils.removePontuacao(s).trim();
				if (ss.length() == 0)
					continue;
				TipoParametro cda = new TipoParametro();
				cda.setNome("NUMEROCDA");
				cda.setValor(ss);
			}
		}

		if (pas != null) {
			for (String s : pas.split(",")) {
				String ss = Utils.removePontuacao(s).trim();
				if (ss.length() == 0)
					continue;
				TipoParametro pa = new TipoParametro();
				pa.setNome("NUMEROPROCESSOADMINISTRATIVO");
				pa.setValor(ss);
			}
		}

		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<String> protocoloRecebimento = new Holder<>();
		Holder<String> dataOperacao = new Holder<>();
		Holder<byte[]> recibo = new Holder<>();
		Holder<List<TipoParametro>> parametro = new Holder<>();

		client.entregarManifestacaoProcessual(idManif, null, null, dadosBasicos, l, dataEnvio, parametros, sucesso,
				mensagem, protocoloRecebimento, dataOperacao, recibo, parametro);
		if (!sucesso.value)
			throw new Exception(mensagem.value);

		DateTime dt = DateTime.parse(dataOperacao.value, dtfMNI);

		boolean sent = false;
		String numProcFormated = "?";
		if (email != null) {
			try {
				String conteudo = "Prezado(a) " + nome
						+ ",\n\nAcusamos o recebimento da petição inicial conforme dados abaixo:"
						+ "\n\nProcesso Autuado Número: " + numProcFormated + "\nProtocolo: "
						+ protocoloRecebimento.value + "\nData/Hora do Protocolo: " + dt.toString(dtfBR)
						+ "\n\nAtenciosamente,\n\nTribunal Regional Federal da 2a Região";
				Correio.enviar(email, "Balcão Virtual: Protocolo de Recebimento", conteudo,
						numProcFormated + "-protocolo-" + protocoloRecebimento.value + ".pdf", "application/pdf",
						recibo.value);
				sent = true;
			} catch (Exception ex) {
				SwaggerUtils.log(SoapMNI.class).error("Email não enviado", ex);
			}
		}

		SwaggerUtils.log(SoapMNI.class)
				.warn("*** Processo: " + numProcFormated + " Petição Inicial protocolada: " + protocoloRecebimento.value
						+ " Por: " + usuario + " Email: " + email + (sent ? "" : " (email não enviado)"));

		return "Protocolo: " + protocoloRecebimento.value + ", Data: " + dt.toString(dtfBR)
				+ (sent ? "" : " (email não enviado)");
	}

}
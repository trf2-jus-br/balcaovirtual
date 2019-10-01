package br.jus.trf2.balcaovirtual;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Node;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;
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
import br.jus.cnj.intercomunicacao_2_2.ModalidadeRepresentanteProcessual;
import br.jus.cnj.intercomunicacao_2_2.TipoAssuntoProcessual;
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
import br.jus.cnj.intercomunicacao_2_2.TipoRepresentanteProcessual;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222_Service;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Aviso;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ListStatus;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ProcessoNumeroAvisoIdReceberPostResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameProcessoNumeroGetResponse;

public class SoapMNI {
	private static final DateTimeFormatter dtfMNI = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	private static final DateTimeFormatter dtfBR = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
	// private static final DateTimeFormatter dtfFILE =
	// DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");

	private static String getNodeString(Node node) {
		try {
			StringWriter writer = new StringWriter();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(new DOMSource(node), new StreamResult(writer));
			String output = writer.toString();
			return output.substring(output.indexOf("?>") + 2);// remove <?xml
																// version="1.0"
																// encoding="UTF-8"?>
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return node.getTextContent();
	}

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

	public static String consultarProcesso(String idConsultante, String senhaConsultante, String sistema,
			String numProc, boolean cabecalho, boolean movimentos, boolean documentos) throws Exception {
		ServicoIntercomunicacao222 client = getClient(sistema);

		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<TipoProcessoJudicial> processo = new Holder<>();

		Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
		requestContext.put("javax.xml.ws.client.receiveTimeout", "3600000");
		requestContext.put("javax.xml.ws.client.connectionTimeout", "5000");

		client.consultarProcesso(idConsultante, senhaConsultante, numProc, null, movimentos, cabecalho, documentos,
				null, sucesso, mensagem, processo);
		if (!sucesso.value)
			throw new Exception(mensagem.value);

		Type collectionType = new TypeToken<List<TipoParametro>>() {
		}.getType();
		Gson gson = new GsonBuilder().registerTypeAdapter(collectionType, new OutroParametroSerializer())
				.setExclusionStrategies(new ConsultaProcessualExclStrat()).create();
		return gson.toJson(processo);
	}

	static ServicoIntercomunicacao222 getClient(String sistema) throws Exception {
		URL url = new URL(Utils.getMniWsdlUrl(sistema));
		// ServicoIntercomunicacao222_Service service = new
		// ServicoIntercomunicacao222_Service(url);

		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service();
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
		// ServicoIntercomunicacao222 port =
		// service.getPort(ServicoIntercomunicacao222.class);

		// String endpointURL =
		// "http://10.50.1.36/eproc/ws/controlador_ws.php?srv=intercomunicacao2.2";
		String endpointURL = Utils.getMniWsdlEndpoint(sistema);
		BindingProvider bp = (BindingProvider) client;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

		Binding binding = bp.getBinding();
		List<Handler> handlerList = binding.getHandlerChain();
		handlerList.add(new Handler() {

			@Override
			public boolean handleMessage(MessageContext context) {
				SOAPMessageContext soapmc = (SOAPMessageContext) context;

				Boolean outboundProperty = (Boolean) soapmc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

				if (outboundProperty.booleanValue()) {
					try {
						Node body = soapmc.getMessage().getSOAPBody().getFirstChild();
						SwaggerUtils.log(this.getClass()).info(getNodeString(body));
						for (int i = 0; i < body.getChildNodes().getLength(); i++) {
							if ("documento1".equals(body.getChildNodes().item(i).getNodeName())) {
								SOAPElement documento1 = (SOAPElement) body.getChildNodes().item(i);
								documento1.setElementQName(new QName("documento"));
							}
						}
					} catch (SOAPException e) {
						e.printStackTrace();
					}
				}

				return true;
			}

			@Override
			public boolean handleFault(MessageContext context) {
				return true;
			}

			@Override
			public void close(MessageContext context) {
			}

		});
		binding.setHandlerChain(handlerList);
		return client;
	}

	public static byte[] obterPecaProcessual(String idConsultante, String senhaConsultante, String sistema,
			String numProc, String documento) throws Exception {
		ServicoIntercomunicacao222 client = getClient(sistema);
		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<TipoProcessoJudicial> processo = new Holder<>();
		List<String> l = new ArrayList<>();
		l.add(documento);

		client.consultarProcesso(idConsultante, senhaConsultante, numProc, null, false, false, false, l, sucesso,
				mensagem, processo);
		if (!sucesso.value)
			throw new Exception(mensagem.value);
		return processo.value.getDocumento().get(0).getConteudo();
	}

	public static void consultarAvisosPendentes(String idConsultante, String senhaConsultante, List<Aviso> list,
			List<ListStatus> status) throws Exception {

		Usuario u = SessionsCreatePost.assertUsuario();

		for (String system : Utils.getSystems()) {
			if (!u.usuarios.containsKey(system))
				continue;
			ServicoIntercomunicacao222 client = getClient(system);
			Holder<Boolean> sucesso = new Holder<>();
			Holder<String> mensagem = new Holder<>();
			Holder<List<TipoAvisoComunicacaoPendente>> aviso = new Holder<>();

			ListStatus ls = new ListStatus();
			ls.system = system;
			status.add(ls);
			Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
			requestContext.put("javax.xml.ws.client.receiveTimeout", "3600000");
			requestContext.put("javax.xml.ws.client.connectionTimeout", "5000");
			try {
				client.consultarAvisosPendentes(null, idConsultante, senhaConsultante, null, sucesso, mensagem, aviso);
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
					i.dataaviso = Utils.parsearApoloDataHoraMinuto(a.getDataDisponibilizacao());
					i.idaviso = a.getIdAviso();
					i.sistema = system;
					i.orgao = Utils.getOrgao(system);
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
							i.datalimiteintimacaoautomatica = Utils.parsearApoloDataHoraMinuto(p.getValor());
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

	public static void consultarTeorComunicacao(String idConsultante, String senhaConsultante, String numProc,
			String idAviso, String sistema, ProcessoNumeroAvisoIdReceberPostResponse resp) throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();
		String email = (String) jwt.get("email");
		String nome = (String) jwt.get("name");
		String usuario = (String) jwt.get("username");

		String numProcFormated = Utils.formatarNumeroProcesso(numProc);

		String system = sistema.toLowerCase();
		ServicoIntercomunicacao222 client = getClient(system);
		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<List<TipoComunicacaoProcessual>> comunicacao = new Holder<>();

		client.consultarTeorComunicacao(idConsultante, idAviso, null, senhaConsultante, sucesso, mensagem, comunicacao);
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
		resp.sistema = sistema;
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

	public static String enviarPeticaoIntercorrente(String idConsultante, String senhaConsultante, String sistema,
			String numProc, String tpDoc, int nvlSigilo, String cpfEncerraPrazos, String nomePdfs, byte pdf[])
			throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();
		String email = (String) jwt.get("email");
		String nome = (String) jwt.get("name");
		String usuario = (String) jwt.get("username");

		if (nomePdfs == null && pdf == null)
			throw new Exception("Não é possível peticionar sem que seja fornecido um PDF");

		String numProcFormated = Utils.formatarNumeroProcesso(numProc);

		String dataEnvio = new DateTime(new Date()).toString("yyyyMMddHHmmss");
		String dirFinal = Utils.getDirFinal();
		ServicoIntercomunicacao222 client = getClient(sistema);
		List<TipoDocumento> l = new ArrayList<>();
		if (nomePdfs != null) {
			for (String nomePdf : nomePdfs.split(",")) {
				TipoDocumento doc = new TipoDocumento();
				doc.setMimetype("application/pdf");
				doc.setDataHora(dataEnvio);
				doc.setNivelSigilo(nvlSigilo == 0 ? 0 : 5);
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

		ArrayList<TipoParametro> parametros = new ArrayList<TipoParametro>();
		if (cpfEncerraPrazos != null) {
			String[] ieps = cpfEncerraPrazos.split(",");
			for (String iep : ieps) {
				TipoParametro identEncerraPrazos = new TipoParametro();
				identEncerraPrazos.setNome("identEncerraPrazos");
				identEncerraPrazos.setValor(iep);
				parametros.add(identEncerraPrazos);
			}

			TipoParametro abrirPrazoAutomaticamente = new TipoParametro();
			abrirPrazoAutomaticamente.setNome("abrirPrazoAutomaticamente");
			abrirPrazoAutomaticamente.setValor("true");
			parametros.add(abrirPrazoAutomaticamente);
		}

		client.entregarManifestacaoProcessual(idConsultante, senhaConsultante, numProc, null, l, dataEnvio, parametros,
				sucesso, mensagem, protocoloRecebimento, dataOperacao, recibo, parametro);
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
				Correio.enviar(email, "Balcão Virtual: Protocolo de Petição Intercorrente", conteudo,
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

	public static class PeticaoInicial {
		String mensagem;
		String protocolo;
		Date data;
		String numProcFormatado;
		String unidade;
	}

	public static PeticaoInicial enviarPeticaoInicial(String idManif, String senhaManif, String sistema,
			String localidade, String especialidade, String classe, String assuntoPrincipal, double valorCausa,
			String cdas, String pas, int nvlSigilo, boolean justicagratuita, boolean tutelaantecipada,
			boolean prioridadeidoso, List<Parte> partes, String nomePdfs, String tpDocPdfs, String nomePoloAtivo,
			String nomePoloPassivo) throws Exception {
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();
		String email = (String) jwt.get("email");
		String nome = (String) jwt.get("name");
		String usuario = (String) jwt.get("username");

		String dataEnvio = new DateTime(new Date()).toString("yyyyMMddHHmmss");
		String dirFinal = Utils.getDirFinal();
		ServicoIntercomunicacao222 client = getClient(sistema);
		List<TipoDocumento> l = new ArrayList<>();
		String tpDocs[] = tpDocPdfs.split(",");
		int i = 0;
		String classificacoes[] = tpDocPdfs.split(",");
		for (String nomePdf : nomePdfs.split(",")) {
			TipoDocumento doc = new TipoDocumento();
			doc.setMimetype("application/pdf");
			doc.setDataHora(dataEnvio);
			doc.setNivelSigilo(nvlSigilo == 0 ? 0 : 5);
			doc.setTipoDocumento(tpDocs[i]);
			// TODO: Substituir esse número mágico pela tabela de tipos de
			// documentos
			// doc.setTipoDocumento("58");
			Path path = Paths.get(dirFinal + "/" + nomePdf + ".pdf");
			byte[] data = Files.readAllBytes(path);
			doc.setConteudo(data);
			TipoParametro classificacao = new TipoParametro();
			classificacao.setNome("CLASSIFICACAO");
			classificacao.setValor(classificacoes[i]);
			doc.getOutroParametro().add(classificacao);
			l.add(doc);
			i++;
		}

		TipoCabecalhoProcesso dadosBasicos = new TipoCabecalhoProcesso();
		dadosBasicos.setNivelSigilo(nvlSigilo == 0 ? 0 : 5);
		TipoParte tp = null;
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
				tqp = TipoQualificacaoPessoa.JURIDICA;
				break;
			case 4:
				if (tp == null)
					throw new Exception("Não há pessoa para vincular ao advogado");
				TipoRepresentanteProcessual rp = new TipoRepresentanteProcessual();
				rp.setNome(parte.nome);
				rp.setInscricao(Utils.removePontuacao(parte.documento));
				rp.setTipoRepresentante(ModalidadeRepresentanteProcessual.A);
				// rp.setNumeroDocumentoPrincipal("11111111111");
				// rp.setIntimacao(false);
				tp.getAdvogado().add(rp);
				tqp = TipoQualificacaoPessoa.ORGAOREPRESENTACAO;
				continue;
			}

			tp = new TipoParte();
			// if (justicagratuita && tqp == TipoQualificacaoPessoa.FISICA)
			// tp.setAssistenciaJudiciaria(true);
			tp.setRelacionamentoProcessual(ModalidadeRelacionamentoProcessual.RP);
			TipoPessoa pess = new TipoPessoa();
			pess.setNome(parte.nome);
			pess.setNumeroDocumentoPrincipal(Utils.removePontuacao(parte.documento));
			// pess.setCidadeNatural("Rio de Janeiro");
			// pess.setEstadoNatural("RJ");
			pess.setTipoPessoa(tqp);
			tp.setPessoa(pess);
			tpp.getParte().add(tp);
		}

		dadosBasicos.setCodigoLocalidade(localidade);
		// dadosBasicos.setClasseProcessual(20);
		dadosBasicos.setValorCausa(valorCausa);
		List<TipoParametro> parametros = dadosBasicos.getOutroParametro();// new
																			// ArrayList<TipoParametro>();

		if (nomePoloAtivo != null) {
			TipoParametro p = new TipoParametro();
			p.setNome("NOMEPOLOATIVO");
			p.setValor(nomePoloAtivo);
			parametros.add(p);
		}

		if (nomePoloPassivo != null) {
			TipoParametro p = new TipoParametro();
			p.setNome("NOMEPOLOPASSIVO");
			p.setValor(nomePoloPassivo);
			parametros.add(p);
		}

		// Classe processual e parâmetro adicional para informar a classe do
		// Apolo
		String aClasse[] = classe.split("\\|");
		dadosBasicos.setClasseProcessual(Integer.parseInt(aClasse[0]));
		if (aClasse.length == 2) {
			TipoParametro p = new TipoParametro();
			p.setNome("CLASSEINTERNA");
			p.setValor(aClasse[1]);
			parametros.add(p);
		}

		// Assunto principal
		TipoAssuntoProcessual tap = new TipoAssuntoProcessual();
		tap.setCodigoNacional(Integer.parseInt(assuntoPrincipal));
		tap.setPrincipal(true);
		dadosBasicos.getAssunto().add(tap);

		if (prioridadeidoso) {
			dadosBasicos.getPrioridade().add("IDOSO");
		}

		if (justicagratuita) {
			TipoParametro jg = new TipoParametro();
			jg.setNome("JUSTICAGRATUITA");
			jg.setValor("TRUE");
			parametros.add(jg);
		}

		if (justicagratuita) {
			TipoParametro jg = new TipoParametro();
			jg.setNome("JUSTICAGRATUITA");
			jg.setValor("TRUE");
			parametros.add(jg);
		}

		if (tutelaantecipada) {
			TipoParametro tla = new TipoParametro();
			if (sistema.contains("eproc")) {
				tla.setNome("CautelaAntecipacaoTutela");
				tla.setValor("1");
			} else {
				tla.setNome("TUTELAANTECIPADA");
				tla.setValor("TRUE");
			}
			parametros.add(tla);
		}

		if (cdas != null) {
			for (String s : cdas.split(",")) {
				String ss = Utils.removePontuacao(s).trim();
				if (ss.length() == 0)
					continue;
				TipoParametro cda = new TipoParametro();
				cda.setNome("NUMEROCDA");
				cda.setValor(ss);
				parametros.add(cda);
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
				parametros.add(pa);
			}
		}

		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<String> protocoloRecebimento = new Holder<>();
		Holder<String> dataOperacao = new Holder<>();
		Holder<byte[]> recibo = new Holder<>();
		Holder<List<TipoParametro>> parametro = new Holder<>();

		client.entregarManifestacaoProcessual(idManif, senhaManif, null, dadosBasicos, l, dataEnvio, parametros,
				sucesso, mensagem, protocoloRecebimento, dataOperacao, recibo, parametro);
		if (!sucesso.value)
			throw new Exception(mensagem.value);

		String numProc = null;
		String numProcFormatado = null;
		for (TipoParametro p : parametro.value) {
			if (p.getNome().equalsIgnoreCase("numerodoprocesso") || p.getNome().equalsIgnoreCase("numeroProcesso")) {
				numProc = p.getValor();
				numProcFormatado = Utils.formatarNumeroProcesso(numProc);
				break;
			}
		}

		String unidade = null;
		try {
			Future<SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroGetResponse>> future = SwaggerCall.callAsync(
					"validar depois de petição inicial", Utils.getApiPassword(sistema), "GET",
					Utils.getApiUrl(sistema) + "/usuario/" + idManif + "/processo/" + numProc, null,
					UsuarioUsernameProcessoNumeroGetResponse.class);
			SwaggerAsyncResponse<UsuarioUsernameProcessoNumeroGetResponse> sar = future.get();
			if (sar.getException() != null)
				throw sar.getException();
			UsuarioUsernameProcessoNumeroGetResponse r = (UsuarioUsernameProcessoNumeroGetResponse) sar.getResp();
			unidade = r.unidade != null ? r.unidade.trim() : null;

		} catch (Exception ex) {
			SwaggerUtils.log(SoapMNI.class).error("Não foi possível obter a unidade", ex);
		}

		DateTime dt = DateTime.parse(dataOperacao.value, dtfMNI);
		String dataProtocoloFormatada = dt.toString(dtfBR);

		boolean sent = false;
		if (email != null) {
			try {
				String conteudo = "Prezado(a) " + nome
						+ ",\n\nAcusamos o recebimento da petição inicial conforme dados abaixo:"
						+ "\n\nProcesso Autuado Número: " + numProcFormatado + "\nProtocolo: "
						+ protocoloRecebimento.value + "\nData/Hora do Protocolo: " + dataProtocoloFormatada
						+ "\n\nAtenciosamente,\n\nTribunal Regional Federal da 2a Região";
				Correio.enviar(email, "Balcão Virtual: Protocolo de Petição Inicial", conteudo,
						numProcFormatado + "-protocolo-" + protocoloRecebimento.value + ".pdf", "application/pdf",
						recibo.value);
				sent = true;
			} catch (Exception ex) {
				SwaggerUtils.log(SoapMNI.class).error("Email não enviado", ex);
			}
		}

		SwaggerUtils.log(SoapMNI.class)
				.warn("*** Processo: " + numProcFormatado + " Petição Inicial protocolada: "
						+ protocoloRecebimento.value + " Por: " + usuario + " Email: " + email
						+ (sent ? "" : " (email não enviado)"));

		PeticaoInicial pi = new PeticaoInicial();

		pi.mensagem = "Protocolo: " + protocoloRecebimento.value + ", Data: " + dataProtocoloFormatada
				+ ", Processo Autuado: " + numProcFormatado + (unidade != null ? " - " + unidade : "")
				+ (sent ? "" : " (email não enviado)");
		pi.protocolo = protocoloRecebimento.value;
		pi.data = dt.toDate();
		pi.numProcFormatado = numProcFormatado;
		pi.unidade = unidade;
		return pi;
	}

}
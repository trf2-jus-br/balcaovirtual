package br.jus.trf2.balcaovirtual;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.ws.Holder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.jus.cnj.intercomunicacao_2_2.TipoCabecalhoProcesso;
import br.jus.cnj.intercomunicacao_2_2.TipoDocumento;
import br.jus.cnj.intercomunicacao_2_2.TipoParametro;
import br.jus.cnj.intercomunicacao_2_2.TipoProcessoJudicial;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222;
import br.jus.cnj.servico_intercomunicacao_2_2.ServicoIntercomunicacao222_Service;

public class SoapMNI {
	private static final Logger log = LoggerFactory.getLogger(SoapMNI.class);
	private static final DateTimeFormatter dtfMNI = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	private static final DateTimeFormatter dtfBR = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

	public static String consultarProcesso(String idManif, String orgao, String numProc) throws Exception {
		String dataEnvio = new DateTime(new Date()).toString("yyyyMMddHHmmss");
		String dirFinal = Utils.getDirFinal();
		URL url = new URL(Utils.getMniWsdlUrl(orgao));
		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
		TipoCabecalhoProcesso dadosBasicos = new TipoCabecalhoProcesso();
		dadosBasicos.setCodigoLocalidade("1");
		Holder<Boolean> sucesso = new Holder<>();
		Holder<String> mensagem = new Holder<>();
		Holder<TipoProcessoJudicial> processo = new Holder<>();
		Holder<List<TipoParametro>> parametro = new Holder<>();

		client.consultarProcesso(idManif, null, numProc, null, true,
				true, true, null, sucesso, mensagem, processo);
		if (!sucesso.value)
			throw new Exception(mensagem.value);
		
		Gson gson = new Gson();
		return gson.toJson(processo);	}

	public static String enviarPeticaoIntercorrente(String idManif, String orgao, String numProc, String tpDoc,
			int nvlSigilo, String nomePdfs) throws Exception {
		String dataEnvio = new DateTime(new Date()).toString("yyyyMMddHHmmss");
		String dirFinal = Utils.getDirFinal();
		URL url = new URL(Utils.getMniWsdlUrl(orgao));
		ServicoIntercomunicacao222_Service service = new ServicoIntercomunicacao222_Service(url);
		ServicoIntercomunicacao222 client = service.getServicoIntercomunicacao222SOAP();
		List<TipoDocumento> l = new ArrayList<>();
		for (String nomePdf : nomePdfs.split(",")) {
			TipoDocumento doc = new TipoDocumento();
			doc.setMimetype("application/pdf");
			doc.setDataHora(dataEnvio);
			doc.setNivelSigilo(nvlSigilo);
			doc.setTipoDocumento(tpDoc);
			// doc.setTipoDocumento("58198");//tpDoc);
			Path path = Paths.get(dirFinal + "/" + nomePdf + ".pdf");
			byte[] data = Files.readAllBytes(path);
			doc.setConteudo(data);
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
		return "Protocolo: " + protocoloRecebimento.value + ", Data: " + dt.toString(dtfBR);
	}

}
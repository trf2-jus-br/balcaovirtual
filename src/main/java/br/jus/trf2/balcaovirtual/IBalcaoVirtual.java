package br.jus.trf2.balcaovirtual;

import java.util.List;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

public interface IBalcaoVirtual {
	public class ConfigTipoPeticaoIntercorrente implements ISwaggerModel {
		public String id;
		public String descricao;
		public String orgao;
	}

	public class Orgao implements ISwaggerModel {
	}

	public class Unidade implements ISwaggerModel {
	}

	public class QuantidadePorData implements ISwaggerModel {
		public String data;
		public String quantidade;
	}

	public class PeticaoIntercorrenteResumo implements ISwaggerModel {
		public String processo;
		public String protocolo;
		public String dataprotocolo;
		public String classe;
		public String unidade;
		public String orgao;
	}

	public class Aviso implements ISwaggerModel {
		public String idaviso;
		public String dataaviso;
		public String processo;
		public String unidade;
		public String unidadenome;
		public String orgao;
		public String localidade;
		public String unidadetipo;
	}

	public class ListStatus implements ISwaggerModel {
		public String system;
		public String errormsg;
		public String stacktrace;
	}

	public class Codigo implements ISwaggerModel {
	}

	public class Descricao implements ISwaggerModel {
	}

	public class DescricaoCompleta implements ISwaggerModel {
	}

	public class Ativo implements ISwaggerModel {
	}

	public class Error implements ISwaggerModel {
		public String error;
	}

	public class SugestaoPostRequest implements ISwaggerRequest {
		public String nome;
		public String email;
		public String mensagem;
	}

	public class SugestaoPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface ISugestaoPost extends ISwaggerMethod {
		public void run(SugestaoPostRequest req, SugestaoPostResponse resp) throws Exception;
	}

	public class SessionsCreatePostRequest implements ISwaggerRequest {
		public String username;
		public String password;
	}

	public class SessionsCreatePostResponse implements ISwaggerResponse {
		public String id_token;
	}

	public interface ISessionsCreatePost extends ISwaggerMethod {
		public void run(SessionsCreatePostRequest req, SessionsCreatePostResponse resp) throws Exception;
	}

	public class ConfigPeticaoIntercorrenteTiposGetRequest implements ISwaggerRequest {
	}

	public class ConfigPeticaoIntercorrenteTiposGetResponse implements ISwaggerResponse {
		public List<ConfigTipoPeticaoIntercorrente> list;
	}

	public interface IConfigPeticaoIntercorrenteTiposGet extends ISwaggerMethod {
		public void run(ConfigPeticaoIntercorrenteTiposGetRequest req, ConfigPeticaoIntercorrenteTiposGetResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroValidarGetRequest implements ISwaggerRequest {
		public String numero;
	}

	public class ProcessoNumeroValidarGetResponse implements ISwaggerResponse {
		public String orgao;
		public String unidade;
	}

	public interface IProcessoNumeroValidarGet extends ISwaggerMethod {
		public void run(ProcessoNumeroValidarGetRequest req, ProcessoNumeroValidarGetResponse resp) throws Exception;
	}

	public class ProcessoNumeroPeticionarPostRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
		public String tipopeticao;
		public String nivelsigilo;
		public String pdfs;
	}

	public class ProcessoNumeroPeticionarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IProcessoNumeroPeticionarPost extends ISwaggerMethod {
		public void run(ProcessoNumeroPeticionarPostRequest req, ProcessoNumeroPeticionarPostResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroConsultarGetRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
	}

	public class ProcessoNumeroConsultarGetResponse implements ISwaggerResponse {
		public byte[] payload;
		public String contenttype;
	}

	public interface IProcessoNumeroConsultarGet extends ISwaggerMethod {
		public void run(ProcessoNumeroConsultarGetRequest req, ProcessoNumeroConsultarGetResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroPecaIdPdfGetRequest implements ISwaggerRequest {
		public String numero;
		public String id;
		public String orgao;
	}

	public class ProcessoNumeroPecaIdPdfGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IProcessoNumeroPecaIdPdfGet extends ISwaggerMethod {
		public void run(ProcessoNumeroPecaIdPdfGetRequest req, ProcessoNumeroPecaIdPdfGetResponse resp)
				throws Exception;
	}

	public class DownloadJwtGetRequest implements ISwaggerRequest {
		public String jwt;
	}

	public class DownloadJwtGetResponse implements ISwaggerResponse {
		public byte[] payload;
		public String contenttype;
	}

	public interface IDownloadJwtGet extends ISwaggerMethod {
		public void run(DownloadJwtGetRequest req, DownloadJwtGetResponse resp) throws Exception;
	}

	public class ArquivoTemporarioPdfGetRequest implements ISwaggerRequest {
		public String pdf;
	}

	public class ArquivoTemporarioPdfGetResponse implements ISwaggerResponse {
		public byte[] payload;
		public String contenttype;
	}

	public interface IArquivoTemporarioPdfGet extends ISwaggerMethod {
		public void run(ArquivoTemporarioPdfGetRequest req, ArquivoTemporarioPdfGetResponse resp) throws Exception;
	}

	public class PeticaoIntercorrenteContarGetRequest implements ISwaggerRequest {
	}

	public class PeticaoIntercorrenteContarGetResponse implements ISwaggerResponse {
		public List<QuantidadePorData> list;
	}

	public interface IPeticaoIntercorrenteContarGet extends ISwaggerMethod {
		public void run(PeticaoIntercorrenteContarGetRequest req, PeticaoIntercorrenteContarGetResponse resp)
				throws Exception;
	}

	public class PeticaoIntercorrenteListarGetRequest implements ISwaggerRequest {
		public String data;
	}

	public class PeticaoIntercorrenteListarGetResponse implements ISwaggerResponse {
		public List<PeticaoIntercorrenteResumo> list;
	}

	public interface IPeticaoIntercorrenteListarGet extends ISwaggerMethod {
		public void run(PeticaoIntercorrenteListarGetRequest req, PeticaoIntercorrenteListarGetResponse resp)
				throws Exception;
	}

	public class AvisoListarGetRequest implements ISwaggerRequest {
	}

	public class AvisoListarGetResponse implements ISwaggerResponse {
		public List<Aviso> list;
		public List<ListStatus> status;
	}

	public interface IAvisoListarGet extends ISwaggerMethod {
		public void run(AvisoListarGetRequest req, AvisoListarGetResponse resp) throws Exception;
	}

	public class ClasseIdGetRequest implements ISwaggerRequest {
		public String id;
		public String orgao;
	}

	public class ClasseIdGetResponse implements ISwaggerResponse {
		public String codigo;
		public String descricao;
		public String descricaocompleta;
		public Boolean ativo;
	}

	public interface IClasseIdGet extends ISwaggerMethod {
		public void run(ClasseIdGetRequest req, ClasseIdGetResponse resp) throws Exception;
	}

	public class AssuntoIdGetRequest implements ISwaggerRequest {
		public String id;
		public String orgao;
	}

	public class AssuntoIdGetResponse implements ISwaggerResponse {
		public String codigo;
		public String descricao;
		public String descricaocompleta;
		public Boolean ativo;
	}

	public interface IAssuntoIdGet extends ISwaggerMethod {
		public void run(AssuntoIdGetRequest req, AssuntoIdGetResponse resp) throws Exception;
	}

}
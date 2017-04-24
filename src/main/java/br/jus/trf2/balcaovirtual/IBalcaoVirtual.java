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
		public String orgao;
		public String numero;
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

}
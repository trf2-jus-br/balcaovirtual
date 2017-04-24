package br.jus.trf2.sistemaprocessual;

import java.util.List;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;

public interface ISistemaProcessual {
	public class Nome implements ISwaggerModel {
	}

	public class CPF implements ISwaggerModel {
	}

	public class Email implements ISwaggerModel {
	}

	public class TipoPeticaoIntercorrente implements ISwaggerModel {
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

	public class AutenticarPostRequest implements ISwaggerRequest {
		public String login;
		public String senha;
	}

	public class AutenticarPostResponse implements ISwaggerResponse {
		public String nome;
		public String cpf;
		public String email;
	}

	public interface IAutenticarPost extends ISwaggerMethod {
		public void run(AutenticarPostRequest req, AutenticarPostResponse resp) throws Exception;
	}

	public class PeticaoIntercorrenteTiposGetRequest implements ISwaggerRequest {
	}

	public class PeticaoIntercorrenteTiposGetResponse implements ISwaggerResponse {
		public List<TipoPeticaoIntercorrente> list;
	}

	public interface IPeticaoIntercorrenteTiposGet extends ISwaggerMethod {
		public void run(PeticaoIntercorrenteTiposGetRequest req, PeticaoIntercorrenteTiposGetResponse resp)
				throws Exception;
	}

	public class ProcessoValidarNumeroGetRequest implements ISwaggerRequest {
		public String numero;
	}

	public class ProcessoValidarNumeroGetResponse implements ISwaggerResponse {
		public String orgao;
		public String unidade;
	}

	public interface IProcessoValidarNumeroGet extends ISwaggerMethod {
		public void run(ProcessoValidarNumeroGetRequest req, ProcessoValidarNumeroGetResponse resp) throws Exception;
	}

}
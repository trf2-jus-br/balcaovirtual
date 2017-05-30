package br.jus.trf2.sistemaprocessual;

import java.io.InputStream;
import java.util.List;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.ISwaggerResponseFile;

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

	public class Contagem implements ISwaggerModel {
		public String data;
		public String quantidade;
	}

	public class PeticaoIntercorrente implements ISwaggerModel {
		public String processo;
		public String protocolo;
		public String dataprotocolo;
		public String classe;
		public String unidade;
		public String orgao;
	}

	public class Numero implements ISwaggerModel {
	}

	public class Orgao implements ISwaggerModel {
	}

	public class Unidade implements ISwaggerModel {
	}

	public class Codigo implements ISwaggerModel {
	}

	public class Descricao implements ISwaggerModel {
	}

	public class Ativo implements ISwaggerModel {
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
		public String numero;
		public String orgao;
		public String unidade;
	}

	public interface IProcessoValidarNumeroGet extends ISwaggerMethod {
		public void run(ProcessoValidarNumeroGetRequest req, ProcessoValidarNumeroGetResponse resp) throws Exception;
	}

	public class ProcessoNumeroPdfGetRequest implements ISwaggerRequest {
		public String numero;
	}

	public class ProcessoNumeroPdfGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
		public String contenttype = "application/pdf";
		public String contentdisposition = "attachment; filename=processo.pdf";

		public Long contentlength;
		public InputStream inputstream;

		public String getContenttype() {
			return contenttype;
		}

		public void setContenttype(String contenttype) {
			this.contenttype = contenttype;
		}

		public String getContentdisposition() {
			return contentdisposition;
		}

		public void setContentdisposition(String contentdisposition) {
			this.contentdisposition = contentdisposition;
		}

		public Long getContentlength() {
			return contentlength;
		}

		public void setContentlength(Long contentlength) {
			this.contentlength = contentlength;
		}

		public InputStream getInputstream() {
			return inputstream;
		}

		public void setInputstream(InputStream inputstream) {
			this.inputstream = inputstream;
		}
	}

	public interface IProcessoNumeroPdfGet extends ISwaggerMethod {
		public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception;
	}

	public class UsuarioWebUsernameGetRequest implements ISwaggerRequest {
		public String username;
	}

	public class UsuarioWebUsernameGetResponse implements ISwaggerResponse {
		public String nome;
		public String cpf;
		public String email;
	}

	public interface IUsuarioWebUsernameGet extends ISwaggerMethod {
		public void run(UsuarioWebUsernameGetRequest req, UsuarioWebUsernameGetResponse resp) throws Exception;
	}

	public class UsuarioWebUsernamePeticaoIntercorrenteContarGetRequest implements ISwaggerRequest {
		public String username;
		public String dias;
	}

	public class UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse implements ISwaggerResponse {
		public List<Contagem> list;
	}

	public interface IUsuarioWebUsernamePeticaoIntercorrenteContarGet extends ISwaggerMethod {
		public void run(UsuarioWebUsernamePeticaoIntercorrenteContarGetRequest req,
				UsuarioWebUsernamePeticaoIntercorrenteContarGetResponse resp) throws Exception;
	}

	public class UsuarioWebUsernamePeticaoIntercorrenteListarGetRequest implements ISwaggerRequest {
		public String username;
		public String data;
	}

	public class UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse implements ISwaggerResponse {
		public List<PeticaoIntercorrente> list;
	}

	public interface IUsuarioWebUsernamePeticaoIntercorrenteListarGet extends ISwaggerMethod {
		public void run(UsuarioWebUsernamePeticaoIntercorrenteListarGetRequest req,
				UsuarioWebUsernamePeticaoIntercorrenteListarGetResponse resp) throws Exception;
	}

	public class ClasseCnjIdGetRequest implements ISwaggerRequest {
		public String id;
		public String orgao;
	}

	public class ClasseCnjIdGetResponse implements ISwaggerResponse {
		public String codigo;
		public String descricao;
		public Boolean ativo;
	}

	public interface IClasseCnjIdGet extends ISwaggerMethod {
		public void run(ClasseCnjIdGetRequest req, ClasseCnjIdGetResponse resp) throws Exception;
	}

	public class AssuntoCnjIdGetRequest implements ISwaggerRequest {
		public String id;
		public String orgao;
	}

	public class AssuntoCnjIdGetResponse implements ISwaggerResponse {
		public String codigo;
		public String descricao;
		public Boolean ativo;
	}

	public interface IAssuntoCnjIdGet extends ISwaggerMethod {
		public void run(AssuntoCnjIdGetRequest req, AssuntoCnjIdGetResponse resp) throws Exception;
	}

}
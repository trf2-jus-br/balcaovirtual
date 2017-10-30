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

	public class Usuario implements ISwaggerModel {
		public String orgao;
		public String codusu;
		public String codusuweb;
		public String codunidade;
		public String nome;
		public String cpf;
		public String email;
		public String perfil;
	}

	public class TipoPeticaoIntercorrente implements ISwaggerModel {
		public String id;
		public String descricao;
		public String orgao;
	}

	public class IdNome implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public class IdNomeClasseCNJ implements ISwaggerModel {
		public String id;
		public String nome;
		public ClasseCNJ classecnj;
	}

	public class ClasseCNJ implements ISwaggerModel {
		public Double codigo;
		public String descricao;
		public Boolean ativo;
	}

	public class Contagem implements ISwaggerModel {
		public String data;
		public String quantidade;
	}

	public class QuantidadeConfirmada implements ISwaggerModel {
		public String data;
		public String quantidadeDoUsuarioPorConfirmacao;
		public String quantidadeDoUsuarioPorOmissao;
		public String quantidadeDoGrupoPorConfirmacao;
		public String quantidadeDoGrupoPorOmissao;
	}

	public class PeticaoIntercorrente implements ISwaggerModel {
		public String processo;
		public String protocolo;
		public String dataprotocolo;
		public String classe;
		public String unidade;
		public String orgao;
	}

	public class Mesa implements ISwaggerModel {
		public String orgao;
		public String idlocal;
		public String idmesa;
		public String nome;
	}

	public class MesaDocumento implements ISwaggerModel {
		public String datadeentrada;
		public String numerodoprocesso;
		public String numerododocumento;
		public String motivo;
		public String situacao;
		public String usuarioinclusao;
	}

	public class MesaMovimento implements ISwaggerModel {
		public String coddoc;
		public String segredo;
		public String numerodoprocesso;
		public String ato;
		public String motivo;
		public String datahoramovimento;
		public String codsecao;
	}

	public class MesaExpediente implements ISwaggerModel {
		public String codsecao;
		public String coddoc;
		public String numerododocumento;
		public String numerodoprocesso;
		public String segredo;
		public String descr;
	}

	public class Numero implements ISwaggerModel {
	}

	public class Orgao implements ISwaggerModel {
	}

	public class Unidade implements ISwaggerModel {
	}

	public class SegredoDeJustica implements ISwaggerModel {
	}

	public class SegredoDeJusticaDeSistema implements ISwaggerModel {
	}

	public class SegredoDeJusticaAbsoluto implements ISwaggerModel {
	}

	public class Eletronico implements ISwaggerModel {
	}

	public class Codigo implements ISwaggerModel {
	}

	public class Descricao implements ISwaggerModel {
	}

	public class Ativo implements ISwaggerModel {
	}

	public class LocalNaUnidade implements ISwaggerModel {
	}

	public class Sentenciado implements ISwaggerModel {
	}

	public class UsuarioAutorizado implements ISwaggerModel {
	}

	public class Baixado implements ISwaggerModel {
	}

	public class CDAs implements ISwaggerModel {
	}

	public class DataUltimoMovimento implements ISwaggerModel {
	}

	public class Error implements ISwaggerModel {
		public String error;
	}

	public class LocalidadeGetRequest implements ISwaggerRequest {
		public String orgao;
	}

	public class LocalidadeGetResponse implements ISwaggerResponse {
		public List<IdNome> list;
	}

	public interface ILocalidadeGet extends ISwaggerMethod {
		public void run(LocalidadeGetRequest req, LocalidadeGetResponse resp) throws Exception;
	}

	public class LocalidadeIdEspecialidadeGetRequest implements ISwaggerRequest {
		public String id;
		public String orgao;
	}

	public class LocalidadeIdEspecialidadeGetResponse implements ISwaggerResponse {
		public List<IdNome> list;
	}

	public interface ILocalidadeIdEspecialidadeGet extends ISwaggerMethod {
		public void run(LocalidadeIdEspecialidadeGetRequest req, LocalidadeIdEspecialidadeGetResponse resp)
				throws Exception;
	}

	public class LocalidadeIdEspecialidadeId2ClasseGetRequest implements ISwaggerRequest {
		public String id;
		public String id2;
		public String orgao;
	}

	public class LocalidadeIdEspecialidadeId2ClasseGetResponse implements ISwaggerResponse {
		public List<IdNomeClasseCNJ> list;
	}

	public interface ILocalidadeIdEspecialidadeId2ClasseGet extends ISwaggerMethod {
		public void run(LocalidadeIdEspecialidadeId2ClasseGetRequest req,
				LocalidadeIdEspecialidadeId2ClasseGetResponse resp) throws Exception;
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
		public String localNaUnidade;
		public Boolean usuarioautorizado;
		public Boolean segredodejustica;
		public Boolean segredodejusticadesistema;
		public Boolean segredodejusticaabsoluto;
		public Boolean eletronico;
		public Boolean sentenciado;
		public Boolean baixado;
		public String cdas;
		public String dataultimomovimento;
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
		public List<Usuario> usuarios;
	}

	public interface IUsuarioWebUsernameGet extends ISwaggerMethod {
		public void run(UsuarioWebUsernameGetRequest req, UsuarioWebUsernameGetResponse resp) throws Exception;
	}

	public class UsuarioWebUsernameAvisoConfirmadoContarGetRequest implements ISwaggerRequest {
		public String username;
		public String dias;
	}

	public class UsuarioWebUsernameAvisoConfirmadoContarGetResponse implements ISwaggerResponse {
		public List<QuantidadeConfirmada> list;
	}

	public interface IUsuarioWebUsernameAvisoConfirmadoContarGet extends ISwaggerMethod {
		public void run(UsuarioWebUsernameAvisoConfirmadoContarGetRequest req,
				UsuarioWebUsernameAvisoConfirmadoContarGetResponse resp) throws Exception;
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

	public class UsuarioWebUsernameAvisoPendenteExportarGetRequest implements ISwaggerRequest {
		public String username;
	}

	public class UsuarioWebUsernameAvisoPendenteExportarGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
		public String contenttype = "application/xml";
		public String contentdisposition = "attachment; filename=avisos.xml";

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

	public interface IUsuarioWebUsernameAvisoPendenteExportarGet extends ISwaggerMethod {
		public void run(UsuarioWebUsernameAvisoPendenteExportarGetRequest req,
				UsuarioWebUsernameAvisoPendenteExportarGetResponse resp) throws Exception;
	}

	public class UsuarioUsernameMesasGetRequest implements ISwaggerRequest {
		public String username;
	}

	public class UsuarioUsernameMesasGetResponse implements ISwaggerResponse {
		public List<Mesa> list;
	}

	public interface IUsuarioUsernameMesasGet extends ISwaggerMethod {
		public void run(UsuarioUsernameMesasGetRequest req, UsuarioUsernameMesasGetResponse resp) throws Exception;
	}

	public class UsuarioUsernameLocalIdMesaId2DocumentosGetRequest implements ISwaggerRequest {
		public String username;
		public String id;
		public String id2;
		public String orgao;
	}

	public class UsuarioUsernameLocalIdMesaId2DocumentosGetResponse implements ISwaggerResponse {
		public List<MesaDocumento> list;
	}

	public interface IUsuarioUsernameLocalIdMesaId2DocumentosGet extends ISwaggerMethod {
		public void run(UsuarioUsernameLocalIdMesaId2DocumentosGetRequest req,
				UsuarioUsernameLocalIdMesaId2DocumentosGetResponse resp) throws Exception;
	}

	public class UsuarioUsernameLocalIdMesaId2MovimentosGetRequest implements ISwaggerRequest {
		public String username;
		public String id;
		public String id2;
		public String orgao;
	}

	public class UsuarioUsernameLocalIdMesaId2MovimentosGetResponse implements ISwaggerResponse {
		public List<MesaMovimento> list;
	}

	public interface IUsuarioUsernameLocalIdMesaId2MovimentosGet extends ISwaggerMethod {
		public void run(UsuarioUsernameLocalIdMesaId2MovimentosGetRequest req,
				UsuarioUsernameLocalIdMesaId2MovimentosGetResponse resp) throws Exception;
	}

	public class UsuarioUsernameLocalIdMesaId2ExpedientesGetRequest implements ISwaggerRequest {
		public String username;
		public String id;
		public String id2;
		public String orgao;
	}

	public class UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse implements ISwaggerResponse {
		public List<MesaExpediente> list;
	}

	public interface IUsuarioUsernameLocalIdMesaId2ExpedientesGet extends ISwaggerMethod {
		public void run(UsuarioUsernameLocalIdMesaId2ExpedientesGetRequest req,
				UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse resp) throws Exception;
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
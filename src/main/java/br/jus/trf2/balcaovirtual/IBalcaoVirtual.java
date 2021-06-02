package br.jus.trf2.balcaovirtual;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.ISwaggerResponseFile;

public interface IBalcaoVirtual {
	public static class ConfigTipoPeticaoIntercorrente implements ISwaggerModel {
		public String id;
		public String descricao;
		public String sistema;
	}

	public static class ConfigAvisoPeticaoIntercorrente implements ISwaggerModel {
		public String id;
		public String evento;
		public Date data;
	}

	public static class Entidade implements ISwaggerModel {
		public String sistema;
		public String id;
		public String nome;
		public String documento;
		public String tipodedocumento;
	}

	public static class Localidade implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public static class Especialidade implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public static class Classe implements ISwaggerModel {
		public String id;
		public String nome;
		public Boolean valordacausaobrigatorio;
	}

	public static class Assunto implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public static class SistemaInfo implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public static class Mesa implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public static class Numero implements ISwaggerModel {
	}

	public static class Sistema implements ISwaggerModel {
	}

	public static class Orgao implements ISwaggerModel {
	}

	public static class Unidade implements ISwaggerModel {
	}

	public static class SegredoDeJustica implements ISwaggerModel {
	}

	public static class SegredoDeJusticaDeSistema implements ISwaggerModel {
	}

	public static class SegredoDeJusticaAbsoluto implements ISwaggerModel {
	}

	public static class Digital implements ISwaggerModel {
	}

	public static class LocalNaUnidade implements ISwaggerModel {
	}

	public static class Sentenciado implements ISwaggerModel {
	}

	public static class UsuarioAutorizado implements ISwaggerModel {
	}

	public static class Baixado implements ISwaggerModel {
	}

	public static class CDAs implements ISwaggerModel {
	}

	public static class DataUltimoMovimento implements ISwaggerModel {
	}

	public static class DataValidacao implements ISwaggerModel {
	}

	public static class Token implements ISwaggerModel {
	}

	public static class QuantidadePorData implements ISwaggerModel {
		public String data;
		public String quantidade;
	}

	public static class QuantidadeConfirmadaPorData implements ISwaggerModel {
		public String data;
		public String quantidadedousuarioporconfirmacao;
		public String quantidadedousuarioporomissao;
		public String quantidadedogrupoporconfirmacao;
		public String quantidadedogrupoporomissao;
	}

	public static class PeticaoIntercorrenteResumo implements ISwaggerModel {
		public String processo;
		public String protocolo;
		public String dataprotocolo;
		public String classe;
		public String unidade;
		public String sistema;
	}

	public static class Aviso implements ISwaggerModel {
		public String idaviso;
		public Date dataaviso;
		public String tipo;
		public String processo;
		public String unidade;
		public String unidadenome;
		public String unidadetipo;
		public String orgao;
		public String sistema;
		public String localidade;
		public String teor;
		public String eventointimacao;
		public String motivointimacao;
		public String numeroprazo;
		public String tipoprazo;
		public String multiplicadorprazo;
		public Date datalimiteintimacaoautomatica;
		public Date datafinalprazo;
		public String assunto;
		public Date dataconfirmacao;
		public String usuarioconfirmacao;
		public String destinatariotipo;
		public String destinatarionome;
		public String destinatariodocumento;
	}

	public static class Processo implements ISwaggerModel {
		public String numero;
		public Date recente;
		public Boolean favorito;
	}

	public static class ProcessoValido implements ISwaggerModel {
		public String numero;
		public String sistema;
		public String orgao;
		public String unidade;
		public String localnaunidade;
		public Boolean segredodejustica;
		public Boolean segredodejusticadesistema;
		public Boolean segredodejusticaabsoluto;
		public Boolean usuarioautorizado;
		public Boolean digital;
		public Boolean sentenciado;
		public Boolean baixado;
		public String cdas;
		public Date dataultimomovimento;
		public String autor;
		public String reu;
	}

	public static class CDA implements ISwaggerModel {
		public String numero;
		public String processoadministrativo;
		public String status;
		public String grupo;
		public String codigotributo;
		public String tributo;
		public Double valor;
		public Double valorufir;
		public Date dataorigem;
		public Date datainclusao;
	}

	public static class Marca implements ISwaggerModel {
		public String idmarca;
		public String idpeca;
		public String texto;
		public String paginicial;
		public String pagfinal;
		public String idestilo;
		public String nomeusuario;
		public Date dataalteracao;
	}

	public static class Nota implements ISwaggerModel {
		public String idnota;
		public String texto;
		public Boolean pessoal;
		public String nomeusuario;
		public Date dataalteracao;
	}

	public static class MesaDocumento implements ISwaggerModel {
		public String sistema;
		public Date dataDeInclusao;
		public String id;
		public String numeroDoDocumento;
		public String numeroDoProcesso;
		public String autor;
		public String reu;
		public String descricao;
		public String status;
		public String descricaoDoStatus;
		public String tipoDoDocumento;
		public String identificadorDoUsuarioQueIncluiu;
		public String nomeDoUsuarioQueIncluiu;
		public String siglaDaUnidade;
		public String conteudo;
		public String diferencas;
		public Double similaridade;
		public String idPadrao;
		public List<Lembrete> lembretes = new ArrayList<>();
	}

	public static class PadraoItem implements ISwaggerModel {
		public String id;
		public String sistema;
		public Date dataDeInclusao;
		public String numeroDoDocumento;
		public String numeroDoProcesso;
		public String autor;
		public String reu;
		public String descricao;
		public String tipoDoDocumento;
		public String identificadorDoUsuarioQueIncluiu;
		public String nomeDoUsuarioQueIncluiu;
		public String siglaDaUnidade;
		public String conteudo;
		public String titulo;
	}

	public static class Lembrete implements ISwaggerModel {
		public Date dataDeInclusao;
		public String id;
		public String identificadorDoUsuario;
		public String nomeDoUsuario;
		public String conteudo;
	}

	public static class Marcador implements ISwaggerModel {
		public String texto;
	}

	public static class ListStatus implements ISwaggerModel {
		public String system;
		public String errormsg;
		public String stacktrace;
	}

	public static class Status implements ISwaggerModel {
		public String mensagem;
		public Double indice;
		public Double contador;
		public Double bytes;
		public String errormsg;
		public String stacktrace;
	}

	public static class Codigo implements ISwaggerModel {
	}

	public static class Descricao implements ISwaggerModel {
	}

	public static class DescricaoCompleta implements ISwaggerModel {
	}

	public static class Ativo implements ISwaggerModel {
	}

	public static class Id implements ISwaggerModel {
	}

	public static class Nome implements ISwaggerModel {
	}

	public static class Oab implements ISwaggerModel {
	}

	public static class NumeroDoDocumento implements ISwaggerModel {
	}

	public static class TipoDeDocumento implements ISwaggerModel {
	}

	public static class TipoDocumentoPeticaoInicial implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public static class Html implements ISwaggerModel {
	}

	public static class Qs implements ISwaggerModel {
	}

	public static class Params implements ISwaggerModel {
	}

	public static class TipoDeCertidao implements ISwaggerModel {
	}

	public static class Ok implements ISwaggerModel {
	}

	public static class Error implements ISwaggerModel {
		public String errormsg;
	}

	public interface ISugestaoPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String nome;
			public String email;
			public String mensagem;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IAutenticarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String username;
			public String password;
		}

		public static class Response implements ISwaggerResponse {
			public String id_token;
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IStatusChaveGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String chave;
		}

		public static class Response implements ISwaggerResponse {
			public String mensagem;
			public Double indice;
			public Double contador;
			public Double bytes;
			public String errormsg;
			public String stacktrace;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigEntidadesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Entidade> list = new ArrayList<>();
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigSistemasGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<SistemaInfo> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigTiposDocumentoPeticaoInicialGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<TipoDocumentoPeticaoInicial> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigLocalidadesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<Localidade> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigLocalidadeIdEspecialidadesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<Especialidade> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigLocalidadeIdEspecialidadeId2ClassesGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String id2;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<Classe> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigLocalidadeIdEspecialidadeId2ClasseId3AssuntosGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String id2;
			public String id3;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<Assunto> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigPessoaFisicaCpfGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String cpf;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public String sistema;
			public String id;
			public String nome;
			public String documento;
			public String tipodedocumento;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigPessoaJuridicaCnpjGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String cnpj;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public String sistema;
			public String id;
			public String nome;
			public String documento;
			public String tipodedocumento;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IConfigAdvogadoOabGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String oab;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public String sistema;
			public String id;
			public String nome;
			public String oab;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroValidarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String captcha;
			public String token;
		}

		public static class Response implements ISwaggerResponse {
			public List<ProcessoValido> list = new ArrayList<>();
			public Date datavalidacao;
			public String token;
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroConsultarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
			public String token;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
			public String contenttype = "application/pdf";
			public String contentdisposition = "attachment";
			public Long contentlength;
			public InputStream inputstream;
			public Map<String, List<String>> headerFields;

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

			public Map<String, List<String>> getHeaderFields() {
				return headerFields;
			}

			public void setHeaderFields(Map<String, List<String>> headerFields) {
				this.headerFields = headerFields;
			}

		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroInformacoesAdicionaisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<CDA> cdas = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroPecaIdPdfGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String id;
			public String sistema;
			public String token;
		}

		public static class Response implements ISwaggerResponse {
			public String jwt;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroPdfGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
			public String token;
		}

		public static class Response implements ISwaggerResponse {
			public String uuid;
			public String jwt;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IPeticaoInicialProtocolarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String sistema;
			public String localidade;
			public String especialidade;
			public String classe;
			public String assunto;
			public String valorcausa;
			public String cdas;
			public String pas;
			public String nivelsigilo;
			public Boolean justicagratuita;
			public Boolean tutelaantecipada;
			public Boolean prioridadeidoso;
			public String partes;
			public String pdfs;
			public String classificacoes;
			public String nomepoloativo;
			public String nomepolopassivo;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
			public String protocolo;
			public Date data;
			public String numero;
			public String unidade;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroPeticaoIntercorrenteValidarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public String identencerraprazos;
			public Double sigilo;
			public Boolean parte;
			public List<ConfigTipoPeticaoIntercorrente> tipos = new ArrayList<>();
			public List<ConfigAvisoPeticaoIntercorrente> avisos = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroPeticionarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
			public String tipopeticao;
			public String nivelsigilo;
			public String encerraprazos;
			public String observacoes;
			public String pdfs;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroCotaPrevisaoPdfPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
			public String nivelsigilo;
			public String texto;
			public String cargo;
			public String empresa;
			public String unidade;
		}

		public static class Response implements ISwaggerResponse {
			public String jwt;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroCotaEnviarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
			public String nivelsigilo;
			public String texto;
			public String cargo;
			public String empresa;
			public String unidade;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroAvisoIdReceberPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String id;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public String idaviso;
			public String dataaviso;
			public String tipo;
			public String processo;
			public String sistema;
			public String teor;
			public String datarecebimento;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IClasseIdMarcadoresGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public List<Marcador> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IMarcaIdDelete extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroMarcasGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<Marca> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroPecaIdMarcaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String id;
			public String idclasse;
			public String sistema;
			public String idmarca;
			public String texto;
			public String idestilo;
			public String paginicial;
			public String pagfinal;
		}

		public static class Response implements ISwaggerResponse {
			public Marca marca;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroNotaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<Nota> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroNotaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
			public String texto;
			public Boolean pessoal;
		}

		public static class Response implements ISwaggerResponse {
			public Nota nota;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroNotaIdPut extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String id;
			public String sistema;
			public String texto;
			public Date dataalteracao;
		}

		public static class Response implements ISwaggerResponse {
			public Nota nota;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroNotaIdDelete extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String sistema;
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroSinalizarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public Boolean recente;
			public Boolean favorito;
		}

		public static class Response implements ISwaggerResponse {
			public Processo processo;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoNumeroSinaisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
		}

		public static class Response implements ISwaggerResponse {
			public Processo processo;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IProcessoListarSinaisGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Processo> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IDownloadJwtFilenameGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String jwt;
			public String filename;
			public String disposition;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
			public String contenttype = "application/pdf";
			public String contentdisposition = "attachment";
			public Long contentlength;
			public InputStream inputstream;
			public Map<String, List<String>> headerFields;

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

			public Map<String, List<String>> getHeaderFields() {
				return headerFields;
			}

			public void setHeaderFields(Map<String, List<String>> headerFields) {
				this.headerFields = headerFields;
			}

		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IImprimirFilenamePost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String filename;
			public String disposition;
			public String html;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
			public String contenttype = "application/pdf";
			public String contentdisposition = "attachment";
			public Long contentlength;
			public InputStream inputstream;
			public Map<String, List<String>> headerFields;

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

			public Map<String, List<String>> getHeaderFields() {
				return headerFields;
			}

			public void setHeaderFields(Map<String, List<String>> headerFields) {
				this.headerFields = headerFields;
			}

		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IImprimirFilenameGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String filename;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
			public String contenttype = "application/pdf";
			public String contentdisposition = "attachment";
			public Long contentlength;
			public InputStream inputstream;
			public Map<String, List<String>> headerFields;

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

			public Map<String, List<String>> getHeaderFields() {
				return headerFields;
			}

			public void setHeaderFields(Map<String, List<String>> headerFields) {
				this.headerFields = headerFields;
			}

		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface INotificarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Status> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IArquivoTemporarioPdfGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String pdf;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
			public String contenttype = "application/pdf";
			public String contentdisposition = "attachment";
			public Long contentlength;
			public InputStream inputstream;
			public Map<String, List<String>> headerFields;

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

			public Map<String, List<String>> getHeaderFields() {
				return headerFields;
			}

			public void setHeaderFields(Map<String, List<String>> headerFields) {
				this.headerFields = headerFields;
			}

		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IPeticaoIntercorrenteContarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<QuantidadePorData> list = new ArrayList<>();
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IPeticaoIntercorrenteListarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String data;
		}

		public static class Response implements ISwaggerResponse {
			public List<PeticaoIntercorrenteResumo> list = new ArrayList<>();
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IAvisoListarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public Boolean mni;
		}

		public static class Response implements ISwaggerResponse {
			public List<Aviso> list = new ArrayList<>();
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IAvisoPendenteXmlGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public String jwt;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IAvisoConfirmadoContarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<QuantidadeConfirmadaPorData> list = new ArrayList<>();
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IAvisoConfirmadoListarGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String datainicial;
			public String datafinal;
			public Boolean confirmacao;
			public Boolean omissao;
			public Boolean grupo;
		}

		public static class Response implements ISwaggerResponse {
			public List<Aviso> list = new ArrayList<>();
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IAvisoConfirmadoXmlGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public String jwt;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IMesaGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<Mesa> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IMesaIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public List<MesaDocumento> list = new ArrayList<>();
			public List<ListStatus> status = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IMesaIdDocumentoId2SalvarPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String id2;
			public String sistema;
			public String html;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IMesaIdDocumentoId2AssinarComSenhaPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String id2;
			public String sistema;
			public String username;
			public String password;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
			public String mensagem;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IMesaIdDocumentoId2DevolverPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String id2;
			public String sistema;
			public String lembrete;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IPadraoGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
		}

		public static class Response implements ISwaggerResponse {
			public List<PadraoItem> list = new ArrayList<>();
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IPadraoPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String html;
		}

		public static class Response implements ISwaggerResponse {
			public PadraoItem padrao;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IPadraoIdDelete extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IClasseIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public String codigo;
			public String descricao;
			public String descricaocompleta;
			public Boolean ativo;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IAssuntoIdGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String id;
			public String sistema;
		}

		public static class Response implements ISwaggerResponse {
			public String codigo;
			public String descricao;
			public String descricaocompleta;
			public Boolean ativo;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface IRecursoArquivoGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String arquivo;
		}

		public static class Response implements ISwaggerResponse, ISwaggerResponseFile {
			public String contenttype = "text/xml";
			public String contentdisposition = "attachment";
			public Long contentlength;
			public InputStream inputstream;
			public Map<String, List<String>> headerFields;

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

			public Map<String, List<String>> getHeaderFields() {
				return headerFields;
			}

			public void setHeaderFields(Map<String, List<String>> headerFields) {
				this.headerFields = headerFields;
			}

		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface ICertidaoObterTokenGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String requisitante;
			public String cpfcnpj;
			public String numero;
			public String sistema;
			public String captcha;
		}

		public static class Response implements ISwaggerResponse {
			public String token;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface ICertidaoEmitirRequisitanteCpfcnpjPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String requisitante;
			public String cpfcnpj;
			public String sistema;
			public String token;
		}

		public static class Response implements ISwaggerResponse {
			public String numero;
			public String tipo;
			public String html;
			public String nome;
			public String qs;
			public String params;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface ICertidaoRequererRequisitanteCpfcnpjPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String requisitante;
			public String cpfcnpj;
			public String sistema;
			public String token;
			public String nome;
			public String params;
		}

		public static class Response implements ISwaggerResponse {
			public String numero;
			public String tipo;
			public String html;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface ICertidaoAutenticarNumeroCpfcnpjGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String cpfcnpj;
			public String sistema;
			public String token;
		}

		public static class Response implements ISwaggerResponse {
			public String numero;
			public String tipo;
			public String html;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface ICertidaoReimprimirNumeroCpfcnpjGet extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String numero;
			public String cpfcnpj;
			public String sistema;
			public String token;
		}

		public static class Response implements ISwaggerResponse {
			public String numero;
			public String tipo;
			public String html;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

	public interface INotificacaoIncluirTokenPost extends ISwaggerMethod {
		public static class Request implements ISwaggerRequest {
			public String token;
		}

		public static class Response implements ISwaggerResponse {
			public String status;
		}

		public void run(Request req, Response resp, BalcaoVirtualContext ctx) throws Exception;
	}

}

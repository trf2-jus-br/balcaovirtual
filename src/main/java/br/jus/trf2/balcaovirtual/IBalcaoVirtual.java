package br.jus.trf2.balcaovirtual;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerMethod;
import com.crivano.swaggerservlet.ISwaggerModel;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.ISwaggerResponseFile;
import com.crivano.swaggerservlet.ISwaggerResponsePayload;

public interface IBalcaoVirtual {
	public class ConfigTipoPeticaoIntercorrente implements ISwaggerModel {
		public String id;
		public String descricao;
		public String orgao;
	}

	public class Entidade implements ISwaggerModel {
		public String orgao;
		public String id;
		public String nome;
		public String documento;
		public String tipodedocumento;
	}

	public class Localidade implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public class Especialidade implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public class Classe implements ISwaggerModel {
		public String id;
		public String nome;
		public Boolean valordacausaobrigatorio;
	}

	public class OrgaoInfo implements ISwaggerModel {
		public String id;
		public String nome;
	}

	public class Mesa implements ISwaggerModel {
		public String id;
		public String nome;
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

	public class Digital implements ISwaggerModel {
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

	public class QuantidadePorData implements ISwaggerModel {
		public String data;
		public String quantidade;
	}

	public class QuantidadeConfirmadaPorData implements ISwaggerModel {
		public String data;
		public String quantidadedousuarioporconfirmacao;
		public String quantidadedousuarioporomissao;
		public String quantidadedogrupoporconfirmacao;
		public String quantidadedogrupoporomissao;
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
		public Date dataaviso;
		public String tipo;
		public String processo;
		public String unidade;
		public String unidadenome;
		public String unidadetipo;
		public String orgao;
		public String localidade;
		public String teor;
		public String eventointimacao;
		public String motivointimacao;
		public String numeroprazo;
		public String tipoprazo;
		public String multiplicadorprazo;
		public Date datalimiteintimacaoautomatica;
		public String assunto;
		public Date dataconfirmacao;
		public String usuarioconfirmacao;
	}

	public class Processo implements ISwaggerModel {
		public String numero;
		public Date recente;
		public Boolean favorito;
	}

	public class Marca implements ISwaggerModel {
		public String idmarca;
		public String idpeca;
		public String texto;
		public String paginicial;
		public String pagfinal;
		public String idestilo;
		public String nomeusuario;
		public Date dataalteracao;
	}

	public class Nota implements ISwaggerModel {
		public String idnota;
		public String texto;
		public Boolean pessoal;
		public String nomeusuario;
		public Date dataalteracao;
	}

	public class Documento implements ISwaggerModel {
		public String docsystem;
		public String docid;
		public String docsecret;
		public String doccode;
		public String docdescr;
		public String dockind;
		public String docorigin;
		public String documento;
		public String processo;
		public String motivo;
		public String situacao;
		public String responsavel;
		public Date dataentrada;
	}

	public class Marcador implements ISwaggerModel {
		public String texto;
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

	public class Id implements ISwaggerModel {
	}

	public class Nome implements ISwaggerModel {
	}

	public class Oab implements ISwaggerModel {
	}

	public class NumeroDoDocumento implements ISwaggerModel {
	}

	public class TipoDeDocumento implements ISwaggerModel {
	}

	public class Error implements ISwaggerModel {
		public String errormsg;
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

	public class ConfigEntidadesGetRequest implements ISwaggerRequest {
	}

	public class ConfigEntidadesGetResponse implements ISwaggerResponse {
		public List<Entidade> list;
	}

	public interface IConfigEntidadesGet extends ISwaggerMethod {
		public void run(ConfigEntidadesGetRequest req, ConfigEntidadesGetResponse resp) throws Exception;
	}

	public class ConfigOrgaosGetRequest implements ISwaggerRequest {
	}

	public class ConfigOrgaosGetResponse implements ISwaggerResponse {
		public List<OrgaoInfo> list;
	}

	public interface IConfigOrgaosGet extends ISwaggerMethod {
		public void run(ConfigOrgaosGetRequest req, ConfigOrgaosGetResponse resp) throws Exception;
	}

	public class ConfigLocalidadesGetRequest implements ISwaggerRequest {
		public String orgao;
	}

	public class ConfigLocalidadesGetResponse implements ISwaggerResponse {
		public List<Localidade> list;
	}

	public interface IConfigLocalidadesGet extends ISwaggerMethod {
		public void run(ConfigLocalidadesGetRequest req, ConfigLocalidadesGetResponse resp) throws Exception;
	}

	public class ConfigLocalidadeIdEspecialidadesGetRequest implements ISwaggerRequest {
		public String id;
		public String orgao;
	}

	public class ConfigLocalidadeIdEspecialidadesGetResponse implements ISwaggerResponse {
		public List<Especialidade> list;
	}

	public interface IConfigLocalidadeIdEspecialidadesGet extends ISwaggerMethod {
		public void run(ConfigLocalidadeIdEspecialidadesGetRequest req,
				ConfigLocalidadeIdEspecialidadesGetResponse resp) throws Exception;
	}

	public class ConfigLocalidadeIdEspecialidadeId2ClassesGetRequest implements ISwaggerRequest {
		public String id;
		public String id2;
		public String orgao;
	}

	public class ConfigLocalidadeIdEspecialidadeId2ClassesGetResponse implements ISwaggerResponse {
		public List<Classe> list;
	}

	public interface IConfigLocalidadeIdEspecialidadeId2ClassesGet extends ISwaggerMethod {
		public void run(ConfigLocalidadeIdEspecialidadeId2ClassesGetRequest req,
				ConfigLocalidadeIdEspecialidadeId2ClassesGetResponse resp) throws Exception;
	}

	public class ConfigPessoaFisicaCpfGetRequest implements ISwaggerRequest {
		public String cpf;
		public String orgao;
	}

	public class ConfigPessoaFisicaCpfGetResponse implements ISwaggerResponse {
		public String orgao;
		public String id;
		public String nome;
		public String documento;
		public String tipodedocumento;
	}

	public interface IConfigPessoaFisicaCpfGet extends ISwaggerMethod {
		public void run(ConfigPessoaFisicaCpfGetRequest req, ConfigPessoaFisicaCpfGetResponse resp) throws Exception;
	}

	public class ConfigPessoaJuridicaCnpjGetRequest implements ISwaggerRequest {
		public String cnpj;
		public String orgao;
	}

	public class ConfigPessoaJuridicaCnpjGetResponse implements ISwaggerResponse {
		public String orgao;
		public String id;
		public String nome;
		public String documento;
		public String tipodedocumento;
	}

	public interface IConfigPessoaJuridicaCnpjGet extends ISwaggerMethod {
		public void run(ConfigPessoaJuridicaCnpjGetRequest req, ConfigPessoaJuridicaCnpjGetResponse resp)
				throws Exception;
	}

	public class ConfigAdvogadoOabGetRequest implements ISwaggerRequest {
		public String oab;
		public String orgao;
	}

	public class ConfigAdvogadoOabGetResponse implements ISwaggerResponse {
		public String orgao;
		public String id;
		public String nome;
		public String oab;
	}

	public interface IConfigAdvogadoOabGet extends ISwaggerMethod {
		public void run(ConfigAdvogadoOabGetRequest req, ConfigAdvogadoOabGetResponse resp) throws Exception;
	}

	public class ProcessoNumeroValidarGetRequest implements ISwaggerRequest {
		public String numero;
	}

	public class ProcessoNumeroValidarGetResponse implements ISwaggerResponse {
		public String numero;
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
	}

	public interface IProcessoNumeroValidarGet extends ISwaggerMethod {
		public void run(ProcessoNumeroValidarGetRequest req, ProcessoNumeroValidarGetResponse resp) throws Exception;
	}

	public class ProcessoNumeroConsultarGetRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
	}

	public class ProcessoNumeroConsultarGetResponse implements ISwaggerResponse, ISwaggerResponsePayload {
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

	public class ProcessoNumeroPdfGetRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
	}

	public class ProcessoNumeroPdfGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IProcessoNumeroPdfGet extends ISwaggerMethod {
		public void run(ProcessoNumeroPdfGetRequest req, ProcessoNumeroPdfGetResponse resp) throws Exception;
	}

	public class PeticaoInicialProtocolarPostRequest implements ISwaggerRequest {
		public String orgao;
		public String localidade;
		public String especialidade;
		public String classe;
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

	public class PeticaoInicialProtocolarPostResponse implements ISwaggerResponse {
		public String status;
		public String protocolo;
		public Date data;
		public String numero;
	}

	public interface IPeticaoInicialProtocolarPost extends ISwaggerMethod {
		public void run(PeticaoInicialProtocolarPostRequest req, PeticaoInicialProtocolarPostResponse resp)
				throws Exception;
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

	public class ProcessoNumeroCotaPrevisaoPdfPostRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
		public String nivelsigilo;
		public String texto;
		public String cargo;
		public String empresa;
		public String unidade;
	}

	public class ProcessoNumeroCotaPrevisaoPdfPostResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IProcessoNumeroCotaPrevisaoPdfPost extends ISwaggerMethod {
		public void run(ProcessoNumeroCotaPrevisaoPdfPostRequest req, ProcessoNumeroCotaPrevisaoPdfPostResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroCotaEnviarPostRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
		public String nivelsigilo;
		public String texto;
		public String cargo;
		public String empresa;
		public String unidade;
	}

	public class ProcessoNumeroCotaEnviarPostResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IProcessoNumeroCotaEnviarPost extends ISwaggerMethod {
		public void run(ProcessoNumeroCotaEnviarPostRequest req, ProcessoNumeroCotaEnviarPostResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroAvisoIdReceberPostRequest implements ISwaggerRequest {
		public String numero;
		public String id;
		public String orgao;
	}

	public class ProcessoNumeroAvisoIdReceberPostResponse implements ISwaggerResponse {
		public String idaviso;
		public String dataaviso;
		public String tipo;
		public String processo;
		public String orgao;
		public String teor;
		public String datarecebimento;
	}

	public interface IProcessoNumeroAvisoIdReceberPost extends ISwaggerMethod {
		public void run(ProcessoNumeroAvisoIdReceberPostRequest req, ProcessoNumeroAvisoIdReceberPostResponse resp)
				throws Exception;
	}

	public class ClasseIdMarcadoresGetRequest implements ISwaggerRequest {
		public String id;
	}

	public class ClasseIdMarcadoresGetResponse implements ISwaggerResponse {
		public List<Marcador> list;
	}

	public interface IClasseIdMarcadoresGet extends ISwaggerMethod {
		public void run(ClasseIdMarcadoresGetRequest req, ClasseIdMarcadoresGetResponse resp) throws Exception;
	}

	public class MarcaIdDeleteRequest implements ISwaggerRequest {
		public String id;
	}

	public class MarcaIdDeleteResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IMarcaIdDelete extends ISwaggerMethod {
		public void run(MarcaIdDeleteRequest req, MarcaIdDeleteResponse resp) throws Exception;
	}

	public class ProcessoNumeroMarcasGetRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
	}

	public class ProcessoNumeroMarcasGetResponse implements ISwaggerResponse {
		public List<Marca> list;
	}

	public interface IProcessoNumeroMarcasGet extends ISwaggerMethod {
		public void run(ProcessoNumeroMarcasGetRequest req, ProcessoNumeroMarcasGetResponse resp) throws Exception;
	}

	public class ProcessoNumeroPecaIdMarcaPostRequest implements ISwaggerRequest {
		public String numero;
		public String id;
		public String idclasse;
		public String orgao;
		public String idmarca;
		public String texto;
		public String idestilo;
		public String paginicial;
		public String pagfinal;
	}

	public class ProcessoNumeroPecaIdMarcaPostResponse implements ISwaggerResponse {
		public Marca marca;
	}

	public interface IProcessoNumeroPecaIdMarcaPost extends ISwaggerMethod {
		public void run(ProcessoNumeroPecaIdMarcaPostRequest req, ProcessoNumeroPecaIdMarcaPostResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroNotaGetRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
	}

	public class ProcessoNumeroNotaGetResponse implements ISwaggerResponse {
		public List<Nota> list;
	}

	public interface IProcessoNumeroNotaGet extends ISwaggerMethod {
		public void run(ProcessoNumeroNotaGetRequest req, ProcessoNumeroNotaGetResponse resp) throws Exception;
	}

	public class ProcessoNumeroNotaPostRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
		public String texto;
		public Boolean pessoal;
	}

	public class ProcessoNumeroNotaPostResponse implements ISwaggerResponse {
		public Nota nota;
	}

	public interface IProcessoNumeroNotaPost extends ISwaggerMethod {
		public void run(ProcessoNumeroNotaPostRequest req, ProcessoNumeroNotaPostResponse resp) throws Exception;
	}

	public class ProcessoNumeroNotaIdPutRequest implements ISwaggerRequest {
		public String numero;
		public String id;
		public String orgao;
		public String texto;
		public Date dataalteracao;
	}

	public class ProcessoNumeroNotaIdPutResponse implements ISwaggerResponse {
		public Nota nota;
	}

	public interface IProcessoNumeroNotaIdPut extends ISwaggerMethod {
		public void run(ProcessoNumeroNotaIdPutRequest req, ProcessoNumeroNotaIdPutResponse resp) throws Exception;
	}

	public class ProcessoNumeroNotaIdDeleteRequest implements ISwaggerRequest {
		public String numero;
		public String orgao;
		public String id;
	}

	public class ProcessoNumeroNotaIdDeleteResponse implements ISwaggerResponse {
		public String status;
	}

	public interface IProcessoNumeroNotaIdDelete extends ISwaggerMethod {
		public void run(ProcessoNumeroNotaIdDeleteRequest req, ProcessoNumeroNotaIdDeleteResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroSinalizarPostRequest implements ISwaggerRequest {
		public String numero;
		public Boolean recente;
		public Boolean favorito;
	}

	public class ProcessoNumeroSinalizarPostResponse implements ISwaggerResponse {
		public Processo processo;
	}

	public interface IProcessoNumeroSinalizarPost extends ISwaggerMethod {
		public void run(ProcessoNumeroSinalizarPostRequest req, ProcessoNumeroSinalizarPostResponse resp)
				throws Exception;
	}

	public class ProcessoNumeroSinaisGetRequest implements ISwaggerRequest {
		public String numero;
	}

	public class ProcessoNumeroSinaisGetResponse implements ISwaggerResponse {
		public Processo processo;
	}

	public interface IProcessoNumeroSinaisGet extends ISwaggerMethod {
		public void run(ProcessoNumeroSinaisGetRequest req, ProcessoNumeroSinaisGetResponse resp) throws Exception;
	}

	public class ProcessoListarSinaisGetRequest implements ISwaggerRequest {
	}

	public class ProcessoListarSinaisGetResponse implements ISwaggerResponse {
		public List<Processo> list;
	}

	public interface IProcessoListarSinaisGet extends ISwaggerMethod {
		public void run(ProcessoListarSinaisGetRequest req, ProcessoListarSinaisGetResponse resp) throws Exception;
	}

	public class DownloadJwtFilenameGetRequest implements ISwaggerRequest {
		public String jwt;
		public String filename;
		public String disposition;
	}

	public class DownloadJwtFilenameGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
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

	public interface IDownloadJwtFilenameGet extends ISwaggerMethod {
		public void run(DownloadJwtFilenameGetRequest req, DownloadJwtFilenameGetResponse resp) throws Exception;
	}

	public class ArquivoTemporarioPdfGetRequest implements ISwaggerRequest {
		public String pdf;
	}

	public class ArquivoTemporarioPdfGetResponse implements ISwaggerResponse, ISwaggerResponseFile {
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

	public class AvisoPendenteXmlGetRequest implements ISwaggerRequest {
	}

	public class AvisoPendenteXmlGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IAvisoPendenteXmlGet extends ISwaggerMethod {
		public void run(AvisoPendenteXmlGetRequest req, AvisoPendenteXmlGetResponse resp) throws Exception;
	}

	public class AvisoConfirmadoContarGetRequest implements ISwaggerRequest {
	}

	public class AvisoConfirmadoContarGetResponse implements ISwaggerResponse {
		public List<QuantidadeConfirmadaPorData> list;
	}

	public interface IAvisoConfirmadoContarGet extends ISwaggerMethod {
		public void run(AvisoConfirmadoContarGetRequest req, AvisoConfirmadoContarGetResponse resp) throws Exception;
	}

	public class AvisoConfirmadoListarGetRequest implements ISwaggerRequest {
		public String datainicial;
		public String datafinal;
		public Boolean confirmacao;
		public Boolean omissao;
		public Boolean grupo;
	}

	public class AvisoConfirmadoListarGetResponse implements ISwaggerResponse {
		public List<Aviso> list;
		public List<ListStatus> status;
	}

	public interface IAvisoConfirmadoListarGet extends ISwaggerMethod {
		public void run(AvisoConfirmadoListarGetRequest req, AvisoConfirmadoListarGetResponse resp) throws Exception;
	}

	public class AvisoConfirmadoXmlGetRequest implements ISwaggerRequest {
	}

	public class AvisoConfirmadoXmlGetResponse implements ISwaggerResponse {
		public String jwt;
	}

	public interface IAvisoConfirmadoXmlGet extends ISwaggerMethod {
		public void run(AvisoConfirmadoXmlGetRequest req, AvisoConfirmadoXmlGetResponse resp) throws Exception;
	}

	public class MesaGetRequest implements ISwaggerRequest {
	}

	public class MesaGetResponse implements ISwaggerResponse {
		public List<Mesa> list;
	}

	public interface IMesaGet extends ISwaggerMethod {
		public void run(MesaGetRequest req, MesaGetResponse resp) throws Exception;
	}

	public class MesaIdGetRequest implements ISwaggerRequest {
		public String id;
		public String orgao;
	}

	public class MesaIdGetResponse implements ISwaggerResponse {
		public List<Documento> list;
	}

	public interface IMesaIdGet extends ISwaggerMethod {
		public void run(MesaIdGetRequest req, MesaIdGetResponse resp) throws Exception;
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

package br.jus.cnj.tipos_servico_intercomunicacao_2_2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import br.jus.cnj.intercomunicacao_2_2.TipoAvisoComunicacaoPendente;
import br.jus.cnj.intercomunicacao_2_2.TipoCabecalhoProcesso;
import br.jus.cnj.intercomunicacao_2_2.TipoComunicacaoProcessual;
import br.jus.cnj.intercomunicacao_2_2.TipoDocumento;
import br.jus.cnj.intercomunicacao_2_2.TipoParametro;
import br.jus.cnj.intercomunicacao_2_2.TipoProcessoJudicial;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.jus.cnj.tipos_servico_intercomunicacao_2_2 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _IdentificadorAviso_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "identificadorAviso");
    private final static QName _DataOperacao_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "dataOperacao");
    private final static QName _SenhaRecebedor_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "senhaRecebedor");
    private final static QName _ProtocoloRecebimento_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "protocoloRecebimento");
    private final static QName _HashDocumentos_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "hashDocumentos");
    private final static QName _SenhaManifestante_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "senhaManifestante");
    private final static QName _IdRecebedor_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "idRecebedor");
    private final static QName _HashCabecalho_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "hashCabecalho");
    private final static QName _IdManifestante_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "idManifestante");
    private final static QName _IncluirCabecalho_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "incluirCabecalho");
    private final static QName _DadosBasicos_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "dadosBasicos");
    private final static QName _Recibo_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "recibo");
    private final static QName _HashMovimentacoes_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "hashMovimentacoes");
    private final static QName _NumeroProcesso_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "numeroProcesso");
    private final static QName _Aviso_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "aviso");
    private final static QName _SenhaConsultante_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "senhaConsultante");
    private final static QName _Parametros_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "parametros");
    private final static QName _Documento1_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "documento1");
    private final static QName _Comunicacao_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "comunicacao");
    private final static QName _IdConsultante_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "idConsultante");
    private final static QName _Movimentos_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "movimentos");
    private final static QName _IdRepresentado_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "idRepresentado");
    private final static QName _Protocolo_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "protocolo");
    private final static QName _DataEnvio_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "dataEnvio");
    private final static QName _IncluirDocumentos_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "incluirDocumentos");
    private final static QName _Sucesso_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "sucesso");
    private final static QName _Documento_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "documento");
    private final static QName _Mensagem_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "mensagem");
    private final static QName _DataReferencia_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "dataReferencia");
    private final static QName _OutroParametro_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "outroParametro");
    private final static QName _Parametro_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "parametro");
    private final static QName _Processo_QNAME = new QName("http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", "processo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.jus.cnj.tipos_servico_intercomunicacao_2_2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "identificadorAviso")
    public JAXBElement<String> createIdentificadorAviso(String value) {
        return new JAXBElement<String>(_IdentificadorAviso_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "dataOperacao")
    public JAXBElement<String> createDataOperacao(String value) {
        return new JAXBElement<String>(_DataOperacao_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "senhaRecebedor")
    public JAXBElement<String> createSenhaRecebedor(String value) {
        return new JAXBElement<String>(_SenhaRecebedor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "protocoloRecebimento")
    public JAXBElement<String> createProtocoloRecebimento(String value) {
        return new JAXBElement<String>(_ProtocoloRecebimento_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "hashDocumentos")
    public JAXBElement<String> createHashDocumentos(String value) {
        return new JAXBElement<String>(_HashDocumentos_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "senhaManifestante")
    public JAXBElement<String> createSenhaManifestante(String value) {
        return new JAXBElement<String>(_SenhaManifestante_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "idRecebedor")
    public JAXBElement<String> createIdRecebedor(String value) {
        return new JAXBElement<String>(_IdRecebedor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "hashCabecalho")
    public JAXBElement<String> createHashCabecalho(String value) {
        return new JAXBElement<String>(_HashCabecalho_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "idManifestante")
    public JAXBElement<String> createIdManifestante(String value) {
        return new JAXBElement<String>(_IdManifestante_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "incluirCabecalho", defaultValue = "false")
    public JAXBElement<Boolean> createIncluirCabecalho(Boolean value) {
        return new JAXBElement<Boolean>(_IncluirCabecalho_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoCabecalhoProcesso }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "dadosBasicos")
    public JAXBElement<TipoCabecalhoProcesso> createDadosBasicos(TipoCabecalhoProcesso value) {
        return new JAXBElement<TipoCabecalhoProcesso>(_DadosBasicos_QNAME, TipoCabecalhoProcesso.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "recibo")
    public JAXBElement<byte[]> createRecibo(byte[] value) {
        return new JAXBElement<byte[]>(_Recibo_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "hashMovimentacoes")
    public JAXBElement<String> createHashMovimentacoes(String value) {
        return new JAXBElement<String>(_HashMovimentacoes_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "numeroProcesso")
    public JAXBElement<String> createNumeroProcesso(String value) {
        return new JAXBElement<String>(_NumeroProcesso_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoAvisoComunicacaoPendente }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "aviso")
    public JAXBElement<TipoAvisoComunicacaoPendente> createAviso(TipoAvisoComunicacaoPendente value) {
        return new JAXBElement<TipoAvisoComunicacaoPendente>(_Aviso_QNAME, TipoAvisoComunicacaoPendente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "senhaConsultante")
    public JAXBElement<String> createSenhaConsultante(String value) {
        return new JAXBElement<String>(_SenhaConsultante_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoParametro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "parametros")
    public JAXBElement<TipoParametro> createParametros(TipoParametro value) {
        return new JAXBElement<TipoParametro>(_Parametros_QNAME, TipoParametro.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "documento1")
    public JAXBElement<String> createDocumento1(String value) {
        return new JAXBElement<String>(_Documento1_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoComunicacaoProcessual }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "comunicacao")
    public JAXBElement<TipoComunicacaoProcessual> createComunicacao(TipoComunicacaoProcessual value) {
        return new JAXBElement<TipoComunicacaoProcessual>(_Comunicacao_QNAME, TipoComunicacaoProcessual.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "idConsultante")
    public JAXBElement<String> createIdConsultante(String value) {
        return new JAXBElement<String>(_IdConsultante_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "movimentos", defaultValue = "false")
    public JAXBElement<Boolean> createMovimentos(Boolean value) {
        return new JAXBElement<Boolean>(_Movimentos_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "idRepresentado")
    public JAXBElement<String> createIdRepresentado(String value) {
        return new JAXBElement<String>(_IdRepresentado_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "protocolo")
    public JAXBElement<String> createProtocolo(String value) {
        return new JAXBElement<String>(_Protocolo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "dataEnvio")
    public JAXBElement<String> createDataEnvio(String value) {
        return new JAXBElement<String>(_DataEnvio_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "incluirDocumentos", defaultValue = "false")
    public JAXBElement<Boolean> createIncluirDocumentos(Boolean value) {
        return new JAXBElement<Boolean>(_IncluirDocumentos_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "sucesso")
    public JAXBElement<Boolean> createSucesso(Boolean value) {
        return new JAXBElement<Boolean>(_Sucesso_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "documento")
    public JAXBElement<TipoDocumento> createDocumento(TipoDocumento value) {
        return new JAXBElement<TipoDocumento>(_Documento_QNAME, TipoDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "mensagem")
    public JAXBElement<String> createMensagem(String value) {
        return new JAXBElement<String>(_Mensagem_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "dataReferencia")
    public JAXBElement<String> createDataReferencia(String value) {
        return new JAXBElement<String>(_DataReferencia_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoParametro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "parametro")
    public JAXBElement<TipoParametro> createParametro(TipoParametro value) {
        return new JAXBElement<TipoParametro>(_Parametro_QNAME, TipoParametro.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoProcessoJudicial }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", name = "processo")
    public JAXBElement<TipoProcessoJudicial> createProcesso(TipoProcessoJudicial value) {
        return new JAXBElement<TipoProcessoJudicial>(_Processo_QNAME, TipoProcessoJudicial.class, null, value);
    }

}


package br.jus.cnj.intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoPessoa complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoPessoa">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="outroNome" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="documento" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoDocumentoIdentificacao" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="endereco" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoEndereco" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pessoaRelacionada" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoRelacionamentoPessoal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pessoaVinculada" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoPessoa" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="nome" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sexo" use="required" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}modalidadeGeneroPessoa" />
 *       &lt;attribute name="nomeGenitor" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nomeGenitora" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dataNascimento" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dataObito" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="numeroDocumentoPrincipal" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tipoPessoa" use="required" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoQualificacaoPessoa" />
 *       &lt;attribute name="cidadeNatural" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="estadoNatural" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nacionalidade" type="{http://www.w3.org/2001/XMLSchema}string" default="BR" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoPessoa", propOrder = {
    "outroNome",
    "documento",
    "endereco",
    "pessoaRelacionada",
    "pessoaVinculada"
})
public class TipoPessoa {

    protected List<String> outroNome;
    protected List<TipoDocumentoIdentificacao> documento;
    protected List<TipoEndereco> endereco;
    @XmlElement(nillable = true)
    protected List<TipoRelacionamentoPessoal> pessoaRelacionada;
    protected TipoPessoa pessoaVinculada;
    @XmlAttribute(name = "nome")
    protected String nome;
    @XmlAttribute(name = "sexo", required = true)
    protected ModalidadeGeneroPessoa sexo;
    @XmlAttribute(name = "nomeGenitor")
    protected String nomeGenitor;
    @XmlAttribute(name = "nomeGenitora")
    protected String nomeGenitora;
    @XmlAttribute(name = "dataNascimento")
    protected String dataNascimento;
    @XmlAttribute(name = "dataObito")
    protected String dataObito;
    @XmlAttribute(name = "numeroDocumentoPrincipal")
    protected String numeroDocumentoPrincipal;
    @XmlAttribute(name = "tipoPessoa", required = true)
    protected TipoQualificacaoPessoa tipoPessoa;
    @XmlAttribute(name = "cidadeNatural")
    protected String cidadeNatural;
    @XmlAttribute(name = "estadoNatural")
    protected String estadoNatural;
    @XmlAttribute(name = "nacionalidade")
    protected String nacionalidade;

    /**
     * Gets the value of the outroNome property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outroNome property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutroNome().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOutroNome() {
        if (outroNome == null) {
            outroNome = new ArrayList<String>();
        }
        return this.outroNome;
    }

    /**
     * Gets the value of the documento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoDocumentoIdentificacao }
     * 
     * 
     */
    public List<TipoDocumentoIdentificacao> getDocumento() {
        if (documento == null) {
            documento = new ArrayList<TipoDocumentoIdentificacao>();
        }
        return this.documento;
    }

    /**
     * Gets the value of the endereco property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endereco property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndereco().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoEndereco }
     * 
     * 
     */
    public List<TipoEndereco> getEndereco() {
        if (endereco == null) {
            endereco = new ArrayList<TipoEndereco>();
        }
        return this.endereco;
    }

    /**
     * Gets the value of the pessoaRelacionada property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pessoaRelacionada property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPessoaRelacionada().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoRelacionamentoPessoal }
     * 
     * 
     */
    public List<TipoRelacionamentoPessoal> getPessoaRelacionada() {
        if (pessoaRelacionada == null) {
            pessoaRelacionada = new ArrayList<TipoRelacionamentoPessoal>();
        }
        return this.pessoaRelacionada;
    }

    /**
     * Gets the value of the pessoaVinculada property.
     * 
     * @return
     *     possible object is
     *     {@link TipoPessoa }
     *     
     */
    public TipoPessoa getPessoaVinculada() {
        return pessoaVinculada;
    }

    /**
     * Sets the value of the pessoaVinculada property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoPessoa }
     *     
     */
    public void setPessoaVinculada(TipoPessoa value) {
        this.pessoaVinculada = value;
    }

    /**
     * Gets the value of the nome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the value of the nome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Gets the value of the sexo property.
     * 
     * @return
     *     possible object is
     *     {@link ModalidadeGeneroPessoa }
     *     
     */
    public ModalidadeGeneroPessoa getSexo() {
        return sexo;
    }

    /**
     * Sets the value of the sexo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModalidadeGeneroPessoa }
     *     
     */
    public void setSexo(ModalidadeGeneroPessoa value) {
        this.sexo = value;
    }

    /**
     * Gets the value of the nomeGenitor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeGenitor() {
        return nomeGenitor;
    }

    /**
     * Sets the value of the nomeGenitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeGenitor(String value) {
        this.nomeGenitor = value;
    }

    /**
     * Gets the value of the nomeGenitora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeGenitora() {
        return nomeGenitora;
    }

    /**
     * Sets the value of the nomeGenitora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeGenitora(String value) {
        this.nomeGenitora = value;
    }

    /**
     * Gets the value of the dataNascimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataNascimento() {
        return dataNascimento;
    }

    /**
     * Sets the value of the dataNascimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataNascimento(String value) {
        this.dataNascimento = value;
    }

    /**
     * Gets the value of the dataObito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataObito() {
        return dataObito;
    }

    /**
     * Sets the value of the dataObito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataObito(String value) {
        this.dataObito = value;
    }

    /**
     * Gets the value of the numeroDocumentoPrincipal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroDocumentoPrincipal() {
        return numeroDocumentoPrincipal;
    }

    /**
     * Sets the value of the numeroDocumentoPrincipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroDocumentoPrincipal(String value) {
        this.numeroDocumentoPrincipal = value;
    }

    /**
     * Gets the value of the tipoPessoa property.
     * 
     * @return
     *     possible object is
     *     {@link TipoQualificacaoPessoa }
     *     
     */
    public TipoQualificacaoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    /**
     * Sets the value of the tipoPessoa property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoQualificacaoPessoa }
     *     
     */
    public void setTipoPessoa(TipoQualificacaoPessoa value) {
        this.tipoPessoa = value;
    }

    /**
     * Gets the value of the cidadeNatural property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCidadeNatural() {
        return cidadeNatural;
    }

    /**
     * Sets the value of the cidadeNatural property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCidadeNatural(String value) {
        this.cidadeNatural = value;
    }

    /**
     * Gets the value of the estadoNatural property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoNatural() {
        return estadoNatural;
    }

    /**
     * Sets the value of the estadoNatural property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoNatural(String value) {
        this.estadoNatural = value;
    }

    /**
     * Gets the value of the nacionalidade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNacionalidade() {
        if (nacionalidade == null) {
            return "BR";
        } else {
            return nacionalidade;
        }
    }

    /**
     * Sets the value of the nacionalidade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNacionalidade(String value) {
        this.nacionalidade = value;
    }

}


package br.jus.cnj.intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoCabecalhoProcesso complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoCabecalhoProcesso">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="polo" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoPoloProcessual" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="assunto" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoAssuntoProcessual" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="magistradoAtuante" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="processoVinculado" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoVinculacaoProcessual" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="prioridade" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="outroParametro" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoParametro" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="valorCausa" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="orgaoJulgador" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoOrgaoJulgador" minOccurs="0"/>
 *         &lt;element name="outrosnumeros" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="numero" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="competencia" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="classeProcessual" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="codigoLocalidade" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nivelSigilo" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="intervencaoMP" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="tamanhoProcesso" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="dataAjuizamento" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoCabecalhoProcesso", propOrder = {
    "polo",
    "assunto",
    "magistradoAtuante",
    "processoVinculado",
    "prioridade",
    "outroParametro",
    "valorCausa",
    "orgaoJulgador",
    "outrosnumeros"
})
public class TipoCabecalhoProcesso {

    protected List<TipoPoloProcessual> polo;
    protected List<TipoAssuntoProcessual> assunto;
    protected List<String> magistradoAtuante;
    protected List<TipoVinculacaoProcessual> processoVinculado;
    protected List<String> prioridade;
    protected List<TipoParametro> outroParametro;
    protected Double valorCausa;
    protected TipoOrgaoJulgador orgaoJulgador;
    protected List<String> outrosnumeros;
    @XmlAttribute(name = "numero")
    protected String numero;
    @XmlAttribute(name = "competencia")
    protected Integer competencia;
    @XmlAttribute(name = "classeProcessual", required = true)
    protected int classeProcessual;
    @XmlAttribute(name = "codigoLocalidade")
    protected String codigoLocalidade;
    @XmlAttribute(name = "nivelSigilo", required = true)
    protected int nivelSigilo;
    @XmlAttribute(name = "intervencaoMP")
    protected Boolean intervencaoMP;
    @XmlAttribute(name = "tamanhoProcesso")
    protected Integer tamanhoProcesso;
    @XmlAttribute(name = "dataAjuizamento")
    protected String dataAjuizamento;

    /**
     * Gets the value of the polo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the polo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoPoloProcessual }
     * 
     * 
     */
    public List<TipoPoloProcessual> getPolo() {
        if (polo == null) {
            polo = new ArrayList<TipoPoloProcessual>();
        }
        return this.polo;
    }

    /**
     * Gets the value of the assunto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assunto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssunto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoAssuntoProcessual }
     * 
     * 
     */
    public List<TipoAssuntoProcessual> getAssunto() {
        if (assunto == null) {
            assunto = new ArrayList<TipoAssuntoProcessual>();
        }
        return this.assunto;
    }

    /**
     * Gets the value of the magistradoAtuante property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the magistradoAtuante property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMagistradoAtuante().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMagistradoAtuante() {
        if (magistradoAtuante == null) {
            magistradoAtuante = new ArrayList<String>();
        }
        return this.magistradoAtuante;
    }

    /**
     * Gets the value of the processoVinculado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processoVinculado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessoVinculado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoVinculacaoProcessual }
     * 
     * 
     */
    public List<TipoVinculacaoProcessual> getProcessoVinculado() {
        if (processoVinculado == null) {
            processoVinculado = new ArrayList<TipoVinculacaoProcessual>();
        }
        return this.processoVinculado;
    }

    /**
     * Gets the value of the prioridade property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prioridade property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrioridade().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPrioridade() {
        if (prioridade == null) {
            prioridade = new ArrayList<String>();
        }
        return this.prioridade;
    }

    /**
     * Gets the value of the outroParametro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outroParametro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutroParametro().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoParametro }
     * 
     * 
     */
    public List<TipoParametro> getOutroParametro() {
        if (outroParametro == null) {
            outroParametro = new ArrayList<TipoParametro>();
        }
        return this.outroParametro;
    }

    /**
     * Gets the value of the valorCausa property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValorCausa() {
        return valorCausa;
    }

    /**
     * Sets the value of the valorCausa property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValorCausa(Double value) {
        this.valorCausa = value;
    }

    /**
     * Gets the value of the orgaoJulgador property.
     * 
     * @return
     *     possible object is
     *     {@link TipoOrgaoJulgador }
     *     
     */
    public TipoOrgaoJulgador getOrgaoJulgador() {
        return orgaoJulgador;
    }

    /**
     * Sets the value of the orgaoJulgador property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoOrgaoJulgador }
     *     
     */
    public void setOrgaoJulgador(TipoOrgaoJulgador value) {
        this.orgaoJulgador = value;
    }

    /**
     * Gets the value of the outrosnumeros property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outrosnumeros property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutrosnumeros().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOutrosnumeros() {
        if (outrosnumeros == null) {
            outrosnumeros = new ArrayList<String>();
        }
        return this.outrosnumeros;
    }

    /**
     * Gets the value of the numero property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Gets the value of the competencia property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCompetencia() {
        return competencia;
    }

    /**
     * Sets the value of the competencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCompetencia(Integer value) {
        this.competencia = value;
    }

    /**
     * Gets the value of the classeProcessual property.
     * 
     */
    public int getClasseProcessual() {
        return classeProcessual;
    }

    /**
     * Sets the value of the classeProcessual property.
     * 
     */
    public void setClasseProcessual(int value) {
        this.classeProcessual = value;
    }

    /**
     * Gets the value of the codigoLocalidade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoLocalidade() {
        return codigoLocalidade;
    }

    /**
     * Sets the value of the codigoLocalidade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoLocalidade(String value) {
        this.codigoLocalidade = value;
    }

    /**
     * Gets the value of the nivelSigilo property.
     * 
     */
    public int getNivelSigilo() {
        return nivelSigilo;
    }

    /**
     * Sets the value of the nivelSigilo property.
     * 
     */
    public void setNivelSigilo(int value) {
        this.nivelSigilo = value;
    }

    /**
     * Gets the value of the intervencaoMP property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIntervencaoMP() {
        return intervencaoMP;
    }

    /**
     * Sets the value of the intervencaoMP property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIntervencaoMP(Boolean value) {
        this.intervencaoMP = value;
    }

    /**
     * Gets the value of the tamanhoProcesso property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTamanhoProcesso() {
        return tamanhoProcesso;
    }

    /**
     * Sets the value of the tamanhoProcesso property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTamanhoProcesso(Integer value) {
        this.tamanhoProcesso = value;
    }

    /**
     * Gets the value of the dataAjuizamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataAjuizamento() {
        return dataAjuizamento;
    }

    /**
     * Sets the value of the dataAjuizamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAjuizamento(String value) {
        this.dataAjuizamento = value;
    }

}

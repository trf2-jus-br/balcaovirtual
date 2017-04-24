
package br.jus.cnj.intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoParte complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoParte">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pessoa" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoPessoa" minOccurs="0"/>
 *         &lt;element name="interessePublico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="advogado" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoRepresentanteProcessual" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pessoaProcessualRelacionada" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoParte" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="assistenciaJudiciaria" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="intimacaoPendente" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="relacionamentoProcessual" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}modalidadeRelacionamentoProcessual" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoParte", propOrder = {
    "pessoa",
    "interessePublico",
    "advogado",
    "pessoaProcessualRelacionada"
})
public class TipoParte {

    protected TipoPessoa pessoa;
    protected String interessePublico;
    @XmlElement(nillable = true)
    protected List<TipoRepresentanteProcessual> advogado;
    @XmlElement(nillable = true)
    protected List<TipoParte> pessoaProcessualRelacionada;
    @XmlAttribute(name = "assistenciaJudiciaria")
    protected Boolean assistenciaJudiciaria;
    @XmlAttribute(name = "intimacaoPendente")
    protected Integer intimacaoPendente;
    @XmlAttribute(name = "relacionamentoProcessual")
    protected ModalidadeRelacionamentoProcessual relacionamentoProcessual;

    /**
     * Gets the value of the pessoa property.
     * 
     * @return
     *     possible object is
     *     {@link TipoPessoa }
     *     
     */
    public TipoPessoa getPessoa() {
        return pessoa;
    }

    /**
     * Sets the value of the pessoa property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoPessoa }
     *     
     */
    public void setPessoa(TipoPessoa value) {
        this.pessoa = value;
    }

    /**
     * Gets the value of the interessePublico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInteressePublico() {
        return interessePublico;
    }

    /**
     * Sets the value of the interessePublico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInteressePublico(String value) {
        this.interessePublico = value;
    }

    /**
     * Gets the value of the advogado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the advogado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdvogado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoRepresentanteProcessual }
     * 
     * 
     */
    public List<TipoRepresentanteProcessual> getAdvogado() {
        if (advogado == null) {
            advogado = new ArrayList<TipoRepresentanteProcessual>();
        }
        return this.advogado;
    }

    /**
     * Gets the value of the pessoaProcessualRelacionada property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pessoaProcessualRelacionada property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPessoaProcessualRelacionada().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoParte }
     * 
     * 
     */
    public List<TipoParte> getPessoaProcessualRelacionada() {
        if (pessoaProcessualRelacionada == null) {
            pessoaProcessualRelacionada = new ArrayList<TipoParte>();
        }
        return this.pessoaProcessualRelacionada;
    }

    /**
     * Gets the value of the assistenciaJudiciaria property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAssistenciaJudiciaria() {
        return assistenciaJudiciaria;
    }

    /**
     * Sets the value of the assistenciaJudiciaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAssistenciaJudiciaria(Boolean value) {
        this.assistenciaJudiciaria = value;
    }

    /**
     * Gets the value of the intimacaoPendente property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIntimacaoPendente() {
        return intimacaoPendente;
    }

    /**
     * Sets the value of the intimacaoPendente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIntimacaoPendente(Integer value) {
        this.intimacaoPendente = value;
    }

    /**
     * Gets the value of the relacionamentoProcessual property.
     * 
     * @return
     *     possible object is
     *     {@link ModalidadeRelacionamentoProcessual }
     *     
     */
    public ModalidadeRelacionamentoProcessual getRelacionamentoProcessual() {
        return relacionamentoProcessual;
    }

    /**
     * Sets the value of the relacionamentoProcessual property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModalidadeRelacionamentoProcessual }
     *     
     */
    public void setRelacionamentoProcessual(ModalidadeRelacionamentoProcessual value) {
        this.relacionamentoProcessual = value;
    }

}

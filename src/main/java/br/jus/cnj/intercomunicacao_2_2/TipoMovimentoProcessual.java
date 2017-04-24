
package br.jus.cnj.intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoMovimentoProcessual complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoMovimentoProcessual">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="complemento" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="movimentoNacional" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoMovimentoNacional" minOccurs="0"/>
 *         &lt;element name="movimentoLocal" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoMovimentoLocal" minOccurs="0"/>
 *         &lt;element name="idDocumentoVinculado" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dataHora" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nivelSigilo" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="identificadorMovimento" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoMovimentoProcessual", propOrder = {
    "complemento",
    "movimentoNacional",
    "movimentoLocal",
    "idDocumentoVinculado"
})
public class TipoMovimentoProcessual {

    @XmlElement(nillable = true)
    protected List<String> complemento;
    protected TipoMovimentoNacional movimentoNacional;
    protected TipoMovimentoLocal movimentoLocal;
    @XmlElement(nillable = true)
    protected List<String> idDocumentoVinculado;
    @XmlAttribute(name = "dataHora")
    protected String dataHora;
    @XmlAttribute(name = "nivelSigilo")
    protected Integer nivelSigilo;
    @XmlAttribute(name = "identificadorMovimento")
    protected String identificadorMovimento;

    /**
     * Gets the value of the complemento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the complemento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComplemento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getComplemento() {
        if (complemento == null) {
            complemento = new ArrayList<String>();
        }
        return this.complemento;
    }

    /**
     * Gets the value of the movimentoNacional property.
     * 
     * @return
     *     possible object is
     *     {@link TipoMovimentoNacional }
     *     
     */
    public TipoMovimentoNacional getMovimentoNacional() {
        return movimentoNacional;
    }

    /**
     * Sets the value of the movimentoNacional property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoMovimentoNacional }
     *     
     */
    public void setMovimentoNacional(TipoMovimentoNacional value) {
        this.movimentoNacional = value;
    }

    /**
     * Gets the value of the movimentoLocal property.
     * 
     * @return
     *     possible object is
     *     {@link TipoMovimentoLocal }
     *     
     */
    public TipoMovimentoLocal getMovimentoLocal() {
        return movimentoLocal;
    }

    /**
     * Sets the value of the movimentoLocal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoMovimentoLocal }
     *     
     */
    public void setMovimentoLocal(TipoMovimentoLocal value) {
        this.movimentoLocal = value;
    }

    /**
     * Gets the value of the idDocumentoVinculado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idDocumentoVinculado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdDocumentoVinculado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIdDocumentoVinculado() {
        if (idDocumentoVinculado == null) {
            idDocumentoVinculado = new ArrayList<String>();
        }
        return this.idDocumentoVinculado;
    }

    /**
     * Gets the value of the dataHora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataHora() {
        return dataHora;
    }

    /**
     * Sets the value of the dataHora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataHora(String value) {
        this.dataHora = value;
    }

    /**
     * Gets the value of the nivelSigilo property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNivelSigilo() {
        return nivelSigilo;
    }

    /**
     * Sets the value of the nivelSigilo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNivelSigilo(Integer value) {
        this.nivelSigilo = value;
    }

    /**
     * Gets the value of the identificadorMovimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorMovimento() {
        return identificadorMovimento;
    }

    /**
     * Sets the value of the identificadorMovimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorMovimento(String value) {
        this.identificadorMovimento = value;
    }

}

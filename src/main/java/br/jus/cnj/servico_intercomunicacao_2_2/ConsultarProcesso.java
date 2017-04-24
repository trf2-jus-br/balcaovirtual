
package br.jus.cnj.servico_intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}idConsultante" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}senhaConsultante" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}numeroProcesso" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}dataReferencia" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}movimentos" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}incluirCabecalho" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}incluirDocumentos" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}documento1" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idConsultante",
    "senhaConsultante",
    "numeroProcesso",
    "dataReferencia",
    "movimentos",
    "incluirCabecalho",
    "incluirDocumentos",
    "documento1"
})
@XmlRootElement(name = "consultarProcesso")
public class ConsultarProcesso {

    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String idConsultante;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String senhaConsultante;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String numeroProcesso;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String dataReferencia;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", defaultValue = "false")
    protected Boolean movimentos;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", defaultValue = "false")
    protected Boolean incluirCabecalho;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", defaultValue = "false")
    protected Boolean incluirDocumentos;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", nillable = true)
    protected List<String> documento1;

    /**
     * Gets the value of the idConsultante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdConsultante() {
        return idConsultante;
    }

    /**
     * Sets the value of the idConsultante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdConsultante(String value) {
        this.idConsultante = value;
    }

    /**
     * Gets the value of the senhaConsultante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenhaConsultante() {
        return senhaConsultante;
    }

    /**
     * Sets the value of the senhaConsultante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenhaConsultante(String value) {
        this.senhaConsultante = value;
    }

    /**
     * Gets the value of the numeroProcesso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    /**
     * Sets the value of the numeroProcesso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroProcesso(String value) {
        this.numeroProcesso = value;
    }

    /**
     * Gets the value of the dataReferencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataReferencia() {
        return dataReferencia;
    }

    /**
     * Sets the value of the dataReferencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataReferencia(String value) {
        this.dataReferencia = value;
    }

    /**
     * Gets the value of the movimentos property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMovimentos() {
        return movimentos;
    }

    /**
     * Sets the value of the movimentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMovimentos(Boolean value) {
        this.movimentos = value;
    }

    /**
     * Gets the value of the incluirCabecalho property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncluirCabecalho() {
        return incluirCabecalho;
    }

    /**
     * Sets the value of the incluirCabecalho property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncluirCabecalho(Boolean value) {
        this.incluirCabecalho = value;
    }

    /**
     * Gets the value of the incluirDocumentos property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncluirDocumentos() {
        return incluirDocumentos;
    }

    /**
     * Sets the value of the incluirDocumentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncluirDocumentos(Boolean value) {
        this.incluirDocumentos = value;
    }

    /**
     * Gets the value of the documento1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documento1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumento1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDocumento1() {
        if (documento1 == null) {
            documento1 = new ArrayList<String>();
        }
        return this.documento1;
    }

}

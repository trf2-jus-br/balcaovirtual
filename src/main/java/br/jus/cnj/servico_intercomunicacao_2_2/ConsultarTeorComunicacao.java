
package br.jus.cnj.servico_intercomunicacao_2_2;

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
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}identificadorAviso"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}numeroProcesso" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}senhaConsultante" minOccurs="0"/>
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
    "identificadorAviso",
    "numeroProcesso",
    "senhaConsultante"
})
@XmlRootElement(name = "consultarTeorComunicacao")
public class ConsultarTeorComunicacao {

    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String idConsultante;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", required = true, nillable = true)
    protected String identificadorAviso;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String numeroProcesso;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String senhaConsultante;

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
     * Gets the value of the identificadorAviso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorAviso() {
        return identificadorAviso;
    }

    /**
     * Sets the value of the identificadorAviso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorAviso(String value) {
        this.identificadorAviso = value;
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

}

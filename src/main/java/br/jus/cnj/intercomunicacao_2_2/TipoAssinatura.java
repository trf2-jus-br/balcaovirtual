
package br.jus.cnj.intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoAssinatura complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoAssinatura">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signatarioLogin" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoSignatarioSimples" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="assinatura" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dataAssinatura" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cadeiaCertificado" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="algoritmoHash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codificacaoCertificado" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoAssinatura", propOrder = {
    "signatarioLogin"
})
public class TipoAssinatura {

    protected List<TipoSignatarioSimples> signatarioLogin;
    @XmlAttribute(name = "assinatura")
    protected String assinatura;
    @XmlAttribute(name = "dataAssinatura")
    protected String dataAssinatura;
    @XmlAttribute(name = "cadeiaCertificado")
    protected String cadeiaCertificado;
    @XmlAttribute(name = "algoritmoHash")
    protected String algoritmoHash;
    @XmlAttribute(name = "codificacaoCertificado")
    protected String codificacaoCertificado;

    /**
     * Gets the value of the signatarioLogin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatarioLogin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignatarioLogin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoSignatarioSimples }
     * 
     * 
     */
    public List<TipoSignatarioSimples> getSignatarioLogin() {
        if (signatarioLogin == null) {
            signatarioLogin = new ArrayList<TipoSignatarioSimples>();
        }
        return this.signatarioLogin;
    }

    /**
     * Gets the value of the assinatura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssinatura() {
        return assinatura;
    }

    /**
     * Sets the value of the assinatura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssinatura(String value) {
        this.assinatura = value;
    }

    /**
     * Gets the value of the dataAssinatura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataAssinatura() {
        return dataAssinatura;
    }

    /**
     * Sets the value of the dataAssinatura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataAssinatura(String value) {
        this.dataAssinatura = value;
    }

    /**
     * Gets the value of the cadeiaCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadeiaCertificado() {
        return cadeiaCertificado;
    }

    /**
     * Sets the value of the cadeiaCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadeiaCertificado(String value) {
        this.cadeiaCertificado = value;
    }

    /**
     * Gets the value of the algoritmoHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmoHash() {
        return algoritmoHash;
    }

    /**
     * Sets the value of the algoritmoHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmoHash(String value) {
        this.algoritmoHash = value;
    }

    /**
     * Gets the value of the codificacaoCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodificacaoCertificado() {
        return codificacaoCertificado;
    }

    /**
     * Sets the value of the codificacaoCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodificacaoCertificado(String value) {
        this.codificacaoCertificado = value;
    }

}

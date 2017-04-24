
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoAssuntoProcessual complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoAssuntoProcessual">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoNacional" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="assuntoLocal" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoAssuntoLocal" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="principal" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoAssuntoProcessual", propOrder = {
    "codigoNacional",
    "assuntoLocal"
})
public class TipoAssuntoProcessual {

    protected Integer codigoNacional;
    protected TipoAssuntoLocal assuntoLocal;
    @XmlAttribute(name = "principal")
    protected Boolean principal;

    /**
     * Gets the value of the codigoNacional property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoNacional() {
        return codigoNacional;
    }

    /**
     * Sets the value of the codigoNacional property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoNacional(Integer value) {
        this.codigoNacional = value;
    }

    /**
     * Gets the value of the assuntoLocal property.
     * 
     * @return
     *     possible object is
     *     {@link TipoAssuntoLocal }
     *     
     */
    public TipoAssuntoLocal getAssuntoLocal() {
        return assuntoLocal;
    }

    /**
     * Sets the value of the assuntoLocal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAssuntoLocal }
     *     
     */
    public void setAssuntoLocal(TipoAssuntoLocal value) {
        this.assuntoLocal = value;
    }

    /**
     * Gets the value of the principal property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrincipal() {
        return principal;
    }

    /**
     * Sets the value of the principal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrincipal(Boolean value) {
        this.principal = value;
    }

}

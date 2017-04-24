
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoAssuntoLocal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoAssuntoLocal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="assuntoLocalPai" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoAssuntoLocal" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="codigoAssunto" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="codigoPaiNacional" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoAssuntoLocal", propOrder = {
    "assuntoLocalPai"
})
public class TipoAssuntoLocal {

    protected TipoAssuntoLocal assuntoLocalPai;
    @XmlAttribute(name = "codigoAssunto", required = true)
    protected int codigoAssunto;
    @XmlAttribute(name = "codigoPaiNacional", required = true)
    protected int codigoPaiNacional;
    @XmlAttribute(name = "descricao")
    protected String descricao;

    /**
     * Gets the value of the assuntoLocalPai property.
     * 
     * @return
     *     possible object is
     *     {@link TipoAssuntoLocal }
     *     
     */
    public TipoAssuntoLocal getAssuntoLocalPai() {
        return assuntoLocalPai;
    }

    /**
     * Sets the value of the assuntoLocalPai property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAssuntoLocal }
     *     
     */
    public void setAssuntoLocalPai(TipoAssuntoLocal value) {
        this.assuntoLocalPai = value;
    }

    /**
     * Gets the value of the codigoAssunto property.
     * 
     */
    public int getCodigoAssunto() {
        return codigoAssunto;
    }

    /**
     * Sets the value of the codigoAssunto property.
     * 
     */
    public void setCodigoAssunto(int value) {
        this.codigoAssunto = value;
    }

    /**
     * Gets the value of the codigoPaiNacional property.
     * 
     */
    public int getCodigoPaiNacional() {
        return codigoPaiNacional;
    }

    /**
     * Sets the value of the codigoPaiNacional property.
     * 
     */
    public void setCodigoPaiNacional(int value) {
        this.codigoPaiNacional = value;
    }

    /**
     * Gets the value of the descricao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Sets the value of the descricao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

}

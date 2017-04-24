
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoOrgaoJulgador complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoOrgaoJulgador">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nomeOrgao" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instancia" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="ORIG"/>
 *             &lt;enumeration value="REV"/>
 *             &lt;enumeration value="ESP"/>
 *             &lt;enumeration value="EXT"/>
 *             &lt;enumeration value="ADM"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="codigoMunicipioIBGE" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoOrgaoJulgador")
public class TipoOrgaoJulgador {

    @XmlAttribute(name = "codigoOrgao")
    protected String codigoOrgao;
    @XmlAttribute(name = "nomeOrgao")
    protected String nomeOrgao;
    @XmlAttribute(name = "instancia", required = true)
    protected String instancia;
    @XmlAttribute(name = "codigoMunicipioIBGE", required = true)
    protected int codigoMunicipioIBGE;

    /**
     * Gets the value of the codigoOrgao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrgao() {
        return codigoOrgao;
    }

    /**
     * Sets the value of the codigoOrgao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrgao(String value) {
        this.codigoOrgao = value;
    }

    /**
     * Gets the value of the nomeOrgao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeOrgao() {
        return nomeOrgao;
    }

    /**
     * Sets the value of the nomeOrgao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeOrgao(String value) {
        this.nomeOrgao = value;
    }

    /**
     * Gets the value of the instancia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstancia() {
        return instancia;
    }

    /**
     * Sets the value of the instancia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstancia(String value) {
        this.instancia = value;
    }

    /**
     * Gets the value of the codigoMunicipioIBGE property.
     * 
     */
    public int getCodigoMunicipioIBGE() {
        return codigoMunicipioIBGE;
    }

    /**
     * Sets the value of the codigoMunicipioIBGE property.
     * 
     */
    public void setCodigoMunicipioIBGE(int value) {
        this.codigoMunicipioIBGE = value;
    }

}


package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoVinculacaoProcessual complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoVinculacaoProcessual">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="numeroProcesso" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="vinculo" use="required" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}modalidadeVinculacaoProcesso" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoVinculacaoProcessual")
public class TipoVinculacaoProcessual {

    @XmlAttribute(name = "numeroProcesso")
    protected String numeroProcesso;
    @XmlAttribute(name = "vinculo", required = true)
    protected ModalidadeVinculacaoProcesso vinculo;

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
     * Gets the value of the vinculo property.
     * 
     * @return
     *     possible object is
     *     {@link ModalidadeVinculacaoProcesso }
     *     
     */
    public ModalidadeVinculacaoProcesso getVinculo() {
        return vinculo;
    }

    /**
     * Sets the value of the vinculo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModalidadeVinculacaoProcesso }
     *     
     */
    public void setVinculo(ModalidadeVinculacaoProcesso value) {
        this.vinculo = value;
    }

}

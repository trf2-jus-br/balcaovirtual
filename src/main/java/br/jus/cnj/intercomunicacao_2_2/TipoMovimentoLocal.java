
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoMovimentoLocal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoMovimentoLocal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="movimentoLocalPai" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoMovimentoLocal" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="codigoMovimento" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
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
@XmlType(name = "tipoMovimentoLocal", propOrder = {
    "movimentoLocalPai"
})
public class TipoMovimentoLocal {

    protected TipoMovimentoLocal movimentoLocalPai;
    @XmlAttribute(name = "codigoMovimento", required = true)
    protected int codigoMovimento;
    @XmlAttribute(name = "codigoPaiNacional", required = true)
    protected int codigoPaiNacional;
    @XmlAttribute(name = "descricao")
    protected String descricao;

    /**
     * Gets the value of the movimentoLocalPai property.
     * 
     * @return
     *     possible object is
     *     {@link TipoMovimentoLocal }
     *     
     */
    public TipoMovimentoLocal getMovimentoLocalPai() {
        return movimentoLocalPai;
    }

    /**
     * Sets the value of the movimentoLocalPai property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoMovimentoLocal }
     *     
     */
    public void setMovimentoLocalPai(TipoMovimentoLocal value) {
        this.movimentoLocalPai = value;
    }

    /**
     * Gets the value of the codigoMovimento property.
     * 
     */
    public int getCodigoMovimento() {
        return codigoMovimento;
    }

    /**
     * Sets the value of the codigoMovimento property.
     * 
     */
    public void setCodigoMovimento(int value) {
        this.codigoMovimento = value;
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

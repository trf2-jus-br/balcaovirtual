
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoAvisoComunicacaoPendente complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoAvisoComunicacaoPendente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="destinatario" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoParte" minOccurs="0"/>
 *         &lt;element name="processo" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoCabecalhoProcesso" minOccurs="0"/>
 *         &lt;element name="dataDisponibilizacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="idAviso" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tipoComunicacao" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoAvisoComunicacaoPendente", propOrder = {
    "destinatario",
    "processo",
    "dataDisponibilizacao"
})
public class TipoAvisoComunicacaoPendente {

    protected TipoParte destinatario;
    protected TipoCabecalhoProcesso processo;
    protected String dataDisponibilizacao;
    @XmlAttribute(name = "idAviso")
    protected String idAviso;
    @XmlAttribute(name = "tipoComunicacao")
    protected String tipoComunicacao;

    /**
     * Gets the value of the destinatario property.
     * 
     * @return
     *     possible object is
     *     {@link TipoParte }
     *     
     */
    public TipoParte getDestinatario() {
        return destinatario;
    }

    /**
     * Sets the value of the destinatario property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoParte }
     *     
     */
    public void setDestinatario(TipoParte value) {
        this.destinatario = value;
    }

    /**
     * Gets the value of the processo property.
     * 
     * @return
     *     possible object is
     *     {@link TipoCabecalhoProcesso }
     *     
     */
    public TipoCabecalhoProcesso getProcesso() {
        return processo;
    }

    /**
     * Sets the value of the processo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoCabecalhoProcesso }
     *     
     */
    public void setProcesso(TipoCabecalhoProcesso value) {
        this.processo = value;
    }

    /**
     * Gets the value of the dataDisponibilizacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataDisponibilizacao() {
        return dataDisponibilizacao;
    }

    /**
     * Sets the value of the dataDisponibilizacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataDisponibilizacao(String value) {
        this.dataDisponibilizacao = value;
    }

    /**
     * Gets the value of the idAviso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAviso() {
        return idAviso;
    }

    /**
     * Sets the value of the idAviso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAviso(String value) {
        this.idAviso = value;
    }

    /**
     * Gets the value of the tipoComunicacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComunicacao() {
        return tipoComunicacao;
    }

    /**
     * Sets the value of the tipoComunicacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComunicacao(String value) {
        this.tipoComunicacao = value;
    }

}

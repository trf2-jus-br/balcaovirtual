
package br.jus.cnj.servico_intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import br.jus.cnj.intercomunicacao_2_2.TipoParametro;


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
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}sucesso"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}mensagem" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}protocoloRecebimento" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}dataOperacao" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}recibo" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}parametro" maxOccurs="unbounded" minOccurs="0"/>
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
    "sucesso",
    "mensagem",
    "protocoloRecebimento",
    "dataOperacao",
    "recibo",
    "parametro"
})
@XmlRootElement(name = "entregarManifestacaoProcessualResposta")
public class EntregarManifestacaoProcessualResposta {

    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected boolean sucesso;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String mensagem;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String protocoloRecebimento;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String dataOperacao;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected byte[] recibo;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", nillable = true)
    protected List<TipoParametro> parametro;

    /**
     * Gets the value of the sucesso property.
     * 
     */
    public boolean isSucesso() {
        return sucesso;
    }

    /**
     * Sets the value of the sucesso property.
     * 
     */
    public void setSucesso(boolean value) {
        this.sucesso = value;
    }

    /**
     * Gets the value of the mensagem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Sets the value of the mensagem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensagem(String value) {
        this.mensagem = value;
    }

    /**
     * Gets the value of the protocoloRecebimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocoloRecebimento() {
        return protocoloRecebimento;
    }

    /**
     * Sets the value of the protocoloRecebimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocoloRecebimento(String value) {
        this.protocoloRecebimento = value;
    }

    /**
     * Gets the value of the dataOperacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataOperacao() {
        return dataOperacao;
    }

    /**
     * Sets the value of the dataOperacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataOperacao(String value) {
        this.dataOperacao = value;
    }

    /**
     * Gets the value of the recibo property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRecibo() {
        return recibo;
    }

    /**
     * Sets the value of the recibo property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRecibo(byte[] value) {
        this.recibo = value;
    }

    /**
     * Gets the value of the parametro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parametro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParametro().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoParametro }
     * 
     * 
     */
    public List<TipoParametro> getParametro() {
        if (parametro == null) {
            parametro = new ArrayList<TipoParametro>();
        }
        return this.parametro;
    }

}

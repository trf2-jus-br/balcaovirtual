
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
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}sucesso"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}mensagem" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}hashCabecalho" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}hashMovimentacoes" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}hashDocumentos" minOccurs="0"/>
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
    "hashCabecalho",
    "hashMovimentacoes",
    "hashDocumentos"
})
@XmlRootElement(name = "consultarAlteracaoResposta")
public class ConsultarAlteracaoResposta {

    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected boolean sucesso;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String mensagem;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String hashCabecalho;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String hashMovimentacoes;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String hashDocumentos;

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
     * Gets the value of the hashCabecalho property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashCabecalho() {
        return hashCabecalho;
    }

    /**
     * Sets the value of the hashCabecalho property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashCabecalho(String value) {
        this.hashCabecalho = value;
    }

    /**
     * Gets the value of the hashMovimentacoes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashMovimentacoes() {
        return hashMovimentacoes;
    }

    /**
     * Sets the value of the hashMovimentacoes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashMovimentacoes(String value) {
        this.hashMovimentacoes = value;
    }

    /**
     * Gets the value of the hashDocumentos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashDocumentos() {
        return hashDocumentos;
    }

    /**
     * Sets the value of the hashDocumentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashDocumentos(String value) {
        this.hashDocumentos = value;
    }

}

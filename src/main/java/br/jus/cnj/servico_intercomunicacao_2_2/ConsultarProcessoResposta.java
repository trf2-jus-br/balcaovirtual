
package br.jus.cnj.servico_intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import br.jus.cnj.intercomunicacao_2_2.TipoProcessoJudicial;


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
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}processo" minOccurs="0"/>
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
    "processo"
})
@XmlRootElement(name = "consultarProcessoResposta")
public class ConsultarProcessoResposta {

    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected boolean sucesso;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String mensagem;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected TipoProcessoJudicial processo;

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
     * Gets the value of the processo property.
     * 
     * @return
     *     possible object is
     *     {@link TipoProcessoJudicial }
     *     
     */
    public TipoProcessoJudicial getProcesso() {
        return processo;
    }

    /**
     * Sets the value of the processo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoProcessoJudicial }
     *     
     */
    public void setProcesso(TipoProcessoJudicial value) {
        this.processo = value;
    }

}

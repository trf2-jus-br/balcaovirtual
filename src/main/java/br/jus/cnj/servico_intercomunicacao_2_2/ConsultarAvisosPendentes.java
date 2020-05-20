
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
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}idRepresentado" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}idConsultante" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}senhaConsultante" minOccurs="0"/>
 *         &lt;element ref="{http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2}dataReferencia" minOccurs="0"/>
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
    "idRepresentado",
    "idConsultante",
    "senhaConsultante",
    "dataReferencia", 
    "parametros"
})
@XmlRootElement(name = "consultarAvisosPendentes")
public class ConsultarAvisosPendentes {

    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String idRepresentado;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String idConsultante;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String senhaConsultante;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2")
    protected String dataReferencia;
    @XmlElement(namespace = "http://www.cnj.jus.br/tipos-servico-intercomunicacao-2.2.2", nillable = true)
    protected List<TipoParametro> parametros;

    /**
     * Gets the value of the idRepresentado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdRepresentado() {
        return idRepresentado;
    }

    /**
     * Sets the value of the idRepresentado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdRepresentado(String value) {
        this.idRepresentado = value;
    }

    /**
     * Gets the value of the idConsultante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdConsultante() {
        return idConsultante;
    }

    /**
     * Sets the value of the idConsultante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdConsultante(String value) {
        this.idConsultante = value;
    }

    /**
     * Gets the value of the senhaConsultante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenhaConsultante() {
        return senhaConsultante;
    }

    /**
     * Sets the value of the senhaConsultante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenhaConsultante(String value) {
        this.senhaConsultante = value;
    }

    /**
     * Gets the value of the dataReferencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataReferencia() {
        return dataReferencia;
    }

    /**
     * Sets the value of the dataReferencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataReferencia(String value) {
        this.dataReferencia = value;
    }
    
    public List<TipoParametro> getParametros() {
        if (parametros == null) {
            parametros = new ArrayList<TipoParametro>();
        }
        return this.parametros;
    }

}


package br.jus.cnj.intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoDocumento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoDocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="conteudo" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="assinatura" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoAssinatura" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="outroParametro" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoParametro" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any minOccurs="0"/>
 *         &lt;element name="documentoVinculado" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoDocumento" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="idDocumento" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="idDocumentoVinculado" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dataHora" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mimetype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nivelSigilo" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="movimento" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="hash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tipoDocumentoLocal" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoDocumento", propOrder = {
    "conteudo",
    "assinatura",
    "outroParametro",
    "any",
    "documentoVinculado"
})
public class TipoDocumento {

    protected byte[] conteudo;
    protected List<TipoAssinatura> assinatura;
    protected List<TipoParametro> outroParametro;
    @XmlAnyElement(lax = true)
    protected Object any;
    protected List<TipoDocumento> documentoVinculado;
    @XmlAttribute(name = "idDocumento")
    protected String idDocumento;
    @XmlAttribute(name = "idDocumentoVinculado")
    protected String idDocumentoVinculado;
    @XmlAttribute(name = "tipoDocumento")
    protected String tipoDocumento;
    @XmlAttribute(name = "dataHora")
    protected String dataHora;
    @XmlAttribute(name = "mimetype")
    protected String mimetype;
    @XmlAttribute(name = "nivelSigilo")
    protected Integer nivelSigilo;
    @XmlAttribute(name = "movimento")
    protected Integer movimento;
    @XmlAttribute(name = "hash")
    protected String hash;
    @XmlAttribute(name = "descricao")
    protected String descricao;
    @XmlAttribute(name = "tipoDocumentoLocal")
    protected String tipoDocumentoLocal;

    /**
     * Gets the value of the conteudo property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getConteudo() {
        return conteudo;
    }

    /**
     * Sets the value of the conteudo property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setConteudo(byte[] value) {
        this.conteudo = value;
    }

    /**
     * Gets the value of the assinatura property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assinatura property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssinatura().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoAssinatura }
     * 
     * 
     */
    public List<TipoAssinatura> getAssinatura() {
        if (assinatura == null) {
            assinatura = new ArrayList<TipoAssinatura>();
        }
        return this.assinatura;
    }

    /**
     * Gets the value of the outroParametro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outroParametro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutroParametro().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoParametro }
     * 
     * 
     */
    public List<TipoParametro> getOutroParametro() {
        if (outroParametro == null) {
            outroParametro = new ArrayList<TipoParametro>();
        }
        return this.outroParametro;
    }

    /**
     * Gets the value of the any property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAny(Object value) {
        this.any = value;
    }

    /**
     * Gets the value of the documentoVinculado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentoVinculado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentoVinculado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoDocumento }
     * 
     * 
     */
    public List<TipoDocumento> getDocumentoVinculado() {
        if (documentoVinculado == null) {
            documentoVinculado = new ArrayList<TipoDocumento>();
        }
        return this.documentoVinculado;
    }

    /**
     * Gets the value of the idDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumento() {
        return idDocumento;
    }

    /**
     * Sets the value of the idDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumento(String value) {
        this.idDocumento = value;
    }

    /**
     * Gets the value of the idDocumentoVinculado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumentoVinculado() {
        return idDocumentoVinculado;
    }

    /**
     * Sets the value of the idDocumentoVinculado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumentoVinculado(String value) {
        this.idDocumentoVinculado = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Gets the value of the dataHora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataHora() {
        return dataHora;
    }

    /**
     * Sets the value of the dataHora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataHora(String value) {
        this.dataHora = value;
    }

    /**
     * Gets the value of the mimetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * Sets the value of the mimetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimetype(String value) {
        this.mimetype = value;
    }

    /**
     * Gets the value of the nivelSigilo property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNivelSigilo() {
        return nivelSigilo;
    }

    /**
     * Sets the value of the nivelSigilo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNivelSigilo(Integer value) {
        this.nivelSigilo = value;
    }

    /**
     * Gets the value of the movimento property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMovimento() {
        return movimento;
    }

    /**
     * Sets the value of the movimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMovimento(Integer value) {
        this.movimento = value;
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
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

    /**
     * Gets the value of the tipoDocumentoLocal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumentoLocal() {
        return tipoDocumentoLocal;
    }

    /**
     * Sets the value of the tipoDocumentoLocal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumentoLocal(String value) {
        this.tipoDocumentoLocal = value;
    }

}

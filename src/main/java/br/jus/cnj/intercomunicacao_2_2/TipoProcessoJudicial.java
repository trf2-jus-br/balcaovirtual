
package br.jus.cnj.intercomunicacao_2_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoProcessoJudicial complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoProcessoJudicial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dadosBasicos" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoCabecalhoProcesso" minOccurs="0"/>
 *         &lt;element name="movimento" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoMovimentoProcessual" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="documento" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoDocumento" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoProcessoJudicial", propOrder = {
    "dadosBasicos",
    "movimento",
    "documento"
})
public class TipoProcessoJudicial {

    protected TipoCabecalhoProcesso dadosBasicos;
    protected List<TipoMovimentoProcessual> movimento;
    protected List<TipoDocumento> documento;

    /**
     * Gets the value of the dadosBasicos property.
     * 
     * @return
     *     possible object is
     *     {@link TipoCabecalhoProcesso }
     *     
     */
    public TipoCabecalhoProcesso getDadosBasicos() {
        return dadosBasicos;
    }

    /**
     * Sets the value of the dadosBasicos property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoCabecalhoProcesso }
     *     
     */
    public void setDadosBasicos(TipoCabecalhoProcesso value) {
        this.dadosBasicos = value;
    }

    /**
     * Gets the value of the movimento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the movimento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMovimento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoMovimentoProcessual }
     * 
     * 
     */
    public List<TipoMovimentoProcessual> getMovimento() {
        if (movimento == null) {
            movimento = new ArrayList<TipoMovimentoProcessual>();
        }
        return this.movimento;
    }

    /**
     * Gets the value of the documento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoDocumento }
     * 
     * 
     */
    public List<TipoDocumento> getDocumento() {
        if (documento == null) {
            documento = new ArrayList<TipoDocumento>();
        }
        return this.documento;
    }

}

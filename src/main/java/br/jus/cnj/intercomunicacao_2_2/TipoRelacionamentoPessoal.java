
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoRelacionamentoPessoal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipoRelacionamentoPessoal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pessoa" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}tipoPessoa" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="modalidadeRelacionamento" type="{http://www.cnj.jus.br/intercomunicacao-2.2.2}modalidadesRelacionamentoPessoal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoRelacionamentoPessoal", propOrder = {
    "pessoa"
})
public class TipoRelacionamentoPessoal {

    protected TipoPessoa pessoa;
    @XmlAttribute(name = "modalidadeRelacionamento")
    protected ModalidadesRelacionamentoPessoal modalidadeRelacionamento;

    /**
     * Gets the value of the pessoa property.
     * 
     * @return
     *     possible object is
     *     {@link TipoPessoa }
     *     
     */
    public TipoPessoa getPessoa() {
        return pessoa;
    }

    /**
     * Sets the value of the pessoa property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoPessoa }
     *     
     */
    public void setPessoa(TipoPessoa value) {
        this.pessoa = value;
    }

    /**
     * Gets the value of the modalidadeRelacionamento property.
     * 
     * @return
     *     possible object is
     *     {@link ModalidadesRelacionamentoPessoal }
     *     
     */
    public ModalidadesRelacionamentoPessoal getModalidadeRelacionamento() {
        return modalidadeRelacionamento;
    }

    /**
     * Sets the value of the modalidadeRelacionamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModalidadesRelacionamentoPessoal }
     *     
     */
    public void setModalidadeRelacionamento(ModalidadesRelacionamentoPessoal value) {
        this.modalidadeRelacionamento = value;
    }

}

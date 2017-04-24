
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modalidadeRelacionamentoProcessual.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modalidadeRelacionamentoProcessual">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CP"/>
 *     &lt;enumeration value="RP"/>
 *     &lt;enumeration value="TF"/>
 *     &lt;enumeration value="AT"/>
 *     &lt;enumeration value="AS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "modalidadeRelacionamentoProcessual")
@XmlEnum
public enum ModalidadeRelacionamentoProcessual {

    CP,
    RP,
    TF,
    AT,
    AS;

    public String value() {
        return name();
    }

    public static ModalidadeRelacionamentoProcessual fromValue(String v) {
        return valueOf(v);
    }

}

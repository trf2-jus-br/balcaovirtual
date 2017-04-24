
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modalidadeRepresentanteProcessual.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modalidadeRepresentanteProcessual">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="E"/>
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="P"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "modalidadeRepresentanteProcessual")
@XmlEnum
public enum ModalidadeRepresentanteProcessual {

    A,
    E,
    M,
    D,
    P;

    public String value() {
        return name();
    }

    public static ModalidadeRepresentanteProcessual fromValue(String v) {
        return valueOf(v);
    }

}

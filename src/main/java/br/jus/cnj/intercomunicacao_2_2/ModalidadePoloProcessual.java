
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modalidadePoloProcessual.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modalidadePoloProcessual">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AT"/>
 *     &lt;enumeration value="PA"/>
 *     &lt;enumeration value="TC"/>
 *     &lt;enumeration value="FL"/>
 *     &lt;enumeration value="TJ"/>
 *     &lt;enumeration value="AD"/>
 *     &lt;enumeration value="VI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "modalidadePoloProcessual")
@XmlEnum
public enum ModalidadePoloProcessual {

    AT,
    PA,
    TC,
    FL,
    TJ,
    AD,
    VI;

    public String value() {
        return name();
    }

    public static ModalidadePoloProcessual fromValue(String v) {
        return valueOf(v);
    }

}

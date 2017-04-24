
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modalidadesRelacionamentoPessoal.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modalidadesRelacionamentoPessoal">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="P"/>
 *     &lt;enumeration value="AP"/>
 *     &lt;enumeration value="SP"/>
 *     &lt;enumeration value="T"/>
 *     &lt;enumeration value="C"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "modalidadesRelacionamentoPessoal")
@XmlEnum
public enum ModalidadesRelacionamentoPessoal {

    P,
    AP,
    SP,
    T,
    C;

    public String value() {
        return name();
    }

    public static ModalidadesRelacionamentoPessoal fromValue(String v) {
        return valueOf(v);
    }

}

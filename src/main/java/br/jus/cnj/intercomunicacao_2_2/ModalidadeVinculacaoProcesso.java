
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modalidadeVinculacaoProcesso.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modalidadeVinculacaoProcesso">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CX"/>
 *     &lt;enumeration value="CT"/>
 *     &lt;enumeration value="DP"/>
 *     &lt;enumeration value="OR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "modalidadeVinculacaoProcesso")
@XmlEnum
public enum ModalidadeVinculacaoProcesso {

    CX,
    CT,
    DP,
    OR;

    public String value() {
        return name();
    }

    public static ModalidadeVinculacaoProcesso fromValue(String v) {
        return valueOf(v);
    }

}

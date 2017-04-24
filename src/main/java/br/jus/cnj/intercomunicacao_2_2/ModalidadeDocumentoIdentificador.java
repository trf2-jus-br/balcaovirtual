
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modalidadeDocumentoIdentificador.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modalidadeDocumentoIdentificador">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CI"/>
 *     &lt;enumeration value="CNH"/>
 *     &lt;enumeration value="TE"/>
 *     &lt;enumeration value="CN"/>
 *     &lt;enumeration value="CC"/>
 *     &lt;enumeration value="PAS"/>
 *     &lt;enumeration value="CT"/>
 *     &lt;enumeration value="RIC"/>
 *     &lt;enumeration value="CMF"/>
 *     &lt;enumeration value="PIS_PASEP"/>
 *     &lt;enumeration value="CEI"/>
 *     &lt;enumeration value="NIT"/>
 *     &lt;enumeration value="CP"/>
 *     &lt;enumeration value="IF"/>
 *     &lt;enumeration value="OAB"/>
 *     &lt;enumeration value="RJC"/>
 *     &lt;enumeration value="RGE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "modalidadeDocumentoIdentificador")
@XmlEnum
public enum ModalidadeDocumentoIdentificador {

    CI,
    CNH,
    TE,
    CN,
    CC,
    PAS,
    CT,
    RIC,
    CMF,
    PIS_PASEP,
    CEI,
    NIT,
    CP,
    IF,
    OAB,
    RJC,
    RGE;

    public String value() {
        return name();
    }

    public static ModalidadeDocumentoIdentificador fromValue(String v) {
        return valueOf(v);
    }

}

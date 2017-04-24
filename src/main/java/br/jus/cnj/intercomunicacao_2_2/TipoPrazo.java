
package br.jus.cnj.intercomunicacao_2_2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoPrazo.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoPrazo">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HOR"/>
 *     &lt;enumeration value="DIA"/>
 *     &lt;enumeration value="MES"/>
 *     &lt;enumeration value="ANO"/>
 *     &lt;enumeration value="DATA_CERTA"/>
 *     &lt;enumeration value="SEMPRAZO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoPrazo")
@XmlEnum
public enum TipoPrazo {

    HOR,
    DIA,
    MES,
    ANO,
    DATA_CERTA,
    SEMPRAZO;

    public String value() {
        return name();
    }

    public static TipoPrazo fromValue(String v) {
        return valueOf(v);
    }

}


package bank.services;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for operationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="operationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INTRANSFER"/>
 *     &lt;enumeration value="OUTTRANSFER"/>
 *     &lt;enumeration value="WITHDRAW"/>
 *     &lt;enumeration value="DEPOSIT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "operationType")
@XmlEnum
public enum OperationType {

    INTRANSFER,
    OUTTRANSFER,
    WITHDRAW,
    DEPOSIT;

    public String value() {
        return name();
    }

    public static OperationType fromValue(String v) {
        return valueOf(v);
    }

}

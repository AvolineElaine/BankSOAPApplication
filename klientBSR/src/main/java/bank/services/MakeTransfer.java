
package bank.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for makeTransfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="makeTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceAccount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destinationAccount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "makeTransfer", propOrder = {
    "title",
    "amount",
    "sourceAccount",
    "destinationAccount"
})
public class MakeTransfer {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String amount;
    @XmlElement(required = true)
    protected String sourceAccount;
    @XmlElement(required = true)
    protected String destinationAccount;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the sourceAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceAccount() {
        return sourceAccount;
    }

    /**
     * Sets the value of the sourceAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceAccount(String value) {
        this.sourceAccount = value;
    }

    /**
     * Gets the value of the destinationAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationAccount() {
        return destinationAccount;
    }

    /**
     * Sets the value of the destinationAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationAccount(String value) {
        this.destinationAccount = value;
    }

}

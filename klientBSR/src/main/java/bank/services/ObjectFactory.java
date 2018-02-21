
package bank.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the bank.services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AuthorizationException_QNAME = new QName("http://services.bank/", "AuthorizationException");
    private final static QName _GetAccountsResponse_QNAME = new QName("http://services.bank/", "getAccountsResponse");
    private final static QName _Operation_QNAME = new QName("http://services.bank/", "operation");
    private final static QName _GetAccounts_QNAME = new QName("http://services.bank/", "getAccounts");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: bank.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Account }
     * 
     */
    public Account createAccount() {
        return new Account();
    }

    /**
     * Create an instance of {@link AuthorizationException }
     * 
     */
    public AuthorizationException createAuthorizationException() {
        return new AuthorizationException();
    }

    /**
     * Create an instance of {@link GetAccountsResponse }
     * 
     */
    public GetAccountsResponse createGetAccountsResponse() {
        return new GetAccountsResponse();
    }

    /**
     * Create an instance of {@link GetAccounts }
     * 
     */
    public GetAccounts createGetAccounts() {
        return new GetAccounts();
    }

    /**
     * Create an instance of {@link Operation }
     * 
     */
    public Operation createOperation() {
        return new Operation();
    }

    /**
     * Create an instance of {@link ObjectId }
     * 
     */
    public ObjectId createObjectId() {
        return new ObjectId();
    }

    /**
     * Create an instance of {@link Account.History }
     * 
     */
    public Account.History createAccountHistory() {
        return new Account.History();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bank/", name = "AuthorizationException")
    public JAXBElement<AuthorizationException> createAuthorizationException(AuthorizationException value) {
        return new JAXBElement<AuthorizationException>(_AuthorizationException_QNAME, AuthorizationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bank/", name = "getAccountsResponse")
    public JAXBElement<GetAccountsResponse> createGetAccountsResponse(GetAccountsResponse value) {
        return new JAXBElement<GetAccountsResponse>(_GetAccountsResponse_QNAME, GetAccountsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Operation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bank/", name = "operation")
    public JAXBElement<Operation> createOperation(Operation value) {
        return new JAXBElement<Operation>(_Operation_QNAME, Operation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccounts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.bank/", name = "getAccounts")
    public JAXBElement<GetAccounts> createGetAccounts(GetAccounts value) {
        return new JAXBElement<GetAccounts>(_GetAccounts_QNAME, GetAccounts.class, null, value);
    }

}

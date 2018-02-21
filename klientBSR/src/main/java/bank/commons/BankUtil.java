package bank.commons;

import bank.services.*;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for communicating with SOAP service
*/
 public class BankUtil {
    private static BankUtil instance = new BankUtil();
    private AuthorizationSvc authorizationSvc;
    private AddAccountSvc addAccountSvc;
    private BankOperationSvc bankOperationSvc;
    private DeleteAccountSvc deleteAccountSvc;
    private GetAccountsSvc getAccountsSvc;
    private String token;

    /**
     * Initializes bank service util
     * @throws MalformedURLException when URL is wrong
     */
    public void initialize() throws MalformedURLException {
        URL url = new URL(Constants.AUTH_SERVICE_WSDL_URL);
        QName qName = new QName(Constants.NAMESPACE, "AuthorizationSvcService");
        Service service = Service.create(url, qName);
        authorizationSvc = service.getPort(AuthorizationSvc.class);

        url = new URL(Constants.ADD_ACCOUNT_SERVICE_WSDL_URL);
        qName = new QName(Constants.NAMESPACE, "AddAccountSvcService");
        service = Service.create(url, qName);
        addAccountSvc = service.getPort(AddAccountSvc.class);

        url = new URL(Constants.BANK_OPERATION_SERVICE_WSDL_URL);
        qName = new QName(Constants.NAMESPACE, "BankOperationSvcService");
        service = Service.create(url, qName);
        bankOperationSvc = service.getPort(BankOperationSvc.class);

        url = new URL(Constants.DELETE_ACCOUNT_SERVICE_WSDL_URL);
        qName = new QName(Constants.NAMESPACE, "DeleteAccountSvcService");
        service = Service.create(url, qName);
        deleteAccountSvc = service.getPort(DeleteAccountSvc.class);

        url = new URL(Constants.GET_ACCOUNTS_SERVICE_WSDL_URL);
        qName = new QName(Constants.NAMESPACE, "GetAccountsSvcService");
        service = Service.create(url, qName);
        getAccountsSvc = service.getPort(GetAccountsSvc.class);
    }

    public AuthorizationSvc getAuthorizationSvc() {
        return authorizationSvc;
    }

    public AddAccountSvc getAddAccountSvc() {
        return addAccountSvc;
    }

    public BankOperationSvc getBankOperationSvc() {
        return bankOperationSvc;
    }

    public DeleteAccountSvc getDeleteAccountSvc() {
        return deleteAccountSvc;
    }

    public GetAccountsSvc getGetAccountsSvc() {
        return getAccountsSvc;
    }

    public void setGetAccountsSvc(GetAccountsSvc getAccountsSvc) {
        this.getAccountsSvc = getAccountsSvc;
    }

    public static BankUtil getInstance() {
        return instance;
    }

    public void setToken(String token) {
        this.token = token;

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("token", Collections.singletonList(token));

        Map<String, Object> requestContext = ((BindingProvider) authorizationSvc).getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        requestContext = ((BindingProvider) addAccountSvc).getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        requestContext = ((BindingProvider) bankOperationSvc).getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        requestContext = ((BindingProvider) deleteAccountSvc).getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        requestContext = ((BindingProvider) getAccountsSvc).getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

    public boolean isAuthorized() {
        return token != null;
    }

}

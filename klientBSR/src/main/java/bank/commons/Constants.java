package bank.commons;

/**
 * Constants used in client project
 */

public class Constants {
    private static final int SOAP_PORT = 8002;
    static final String NAMESPACE = "http://services.bank/";
    static final String AUTH_SERVICE_WSDL_URL = "http://localhost:" + SOAP_PORT + "/auth?wsdl";
    static final String GET_ACCOUNTS_SERVICE_WSDL_URL = "http://localhost:" + SOAP_PORT + "/getAccounts?wsdl";
    static final String ADD_ACCOUNT_SERVICE_WSDL_URL = "http://localhost:" + SOAP_PORT + "/addAccount?wsdl";
    static final String DELETE_ACCOUNT_SERVICE_WSDL_URL = "http://localhost:" + SOAP_PORT + "/deleteAccount?wsdl";
    static final String BANK_OPERATION_SERVICE_WSDL_URL = "http://localhost:" + SOAP_PORT + "/bankOperation?wsdl";
}

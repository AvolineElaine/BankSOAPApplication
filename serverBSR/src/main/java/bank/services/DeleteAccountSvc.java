package bank.services;

import bank.commons.DatastoreHandler;
import bank.errorHandling.AuthorizationException;
import bank.models.Account;
import bank.models.Token;
import bank.models.User;
import org.mongodb.morphia.Datastore;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * Web service responsible for deleting bank account
 */
@WebService
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
public class DeleteAccountSvc {
    @Resource
    private WebServiceContext context;

    /**
     * Web method deleting account from account's list
     * @param number number of account which is to remove
     * @throws AuthorizationException when the account is missing
     */
    @WebMethod
    public void deleteBankAccount(@WebParam(name = "number") @XmlElement(required=true) String number) throws AuthorizationException {
        Datastore datastore =  DatastoreHandler.getInstance().getDataStore();
        String token = AuthorizationSvc.getTokenFromHeaders(context);
        Token newToken = AuthorizationSvc.getTokenFromStore(token);
        User user = datastore.find(User.class).field("token").equal(token).get();
        if(user != null) {
            Account account = datastore.find(Account.class).field("number").equal(number).get();
            if(account == null) {
                throw new AuthorizationException("Account not found");
            }
            if(user.containsAccount(account.getNumber())) {
                user.removeBankAccount(account.getNumber());
                datastore.delete(account);
                datastore.save(user);
            } else {
                throw new AuthorizationException("User not authorized to the account");
            }
        } else {
            throw new AuthorizationException("User not found");
        }
    }
}

package bank.services;

import bank.commons.DatastoreHandler;
import bank.errorHandling.AuthorizationException;
import bank.models.Account;
import bank.models.Token;
import bank.models.User;
import org.mongodb.morphia.Datastore;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 * Web service responsible for adding bank account to user
 */

@WebService
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
public class AddAccountSvc {

    @Resource
    private WebServiceContext context;

    /**
     * Web method adding account to the user
     * @throws AuthorizationException when user is missing
     */
    @WebMethod
    public void addBankAccount() throws AuthorizationException {
        Datastore datastore =  DatastoreHandler.getInstance().getDataStore();
        String token = AuthorizationSvc.getTokenFromHeaders(context);
        Token newToken = AuthorizationSvc.getTokenFromStore(token);
        User user = datastore.find(User.class).field("token").equal(token).get();

        if(user != null) {
            Account account = new Account(user.getUsername());
            user.addAccount(account);
            datastore.save(account);
            datastore.save(user);
        } else {
            datastore.delete(newToken);
            throw new AuthorizationException("User not found");
        }
    }
}

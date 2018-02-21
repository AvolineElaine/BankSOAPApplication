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
import java.util.ArrayList;

/**
 * Web service responsible for getting list of user's accounts
 */

@WebService
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
public class GetAccountsSvc {
    @Resource
    private WebServiceContext context;

    /**
     * Web method that gets list of accounts
     * @return list of accounts
     * @throws AuthorizationException when user is missing
     */
    @WebMethod
    public ArrayList<Account> getAccounts() throws AuthorizationException {
        Datastore datastore =  DatastoreHandler.getInstance().getDataStore();
        String token = AuthorizationSvc.getTokenFromHeaders(context);
        Token newToken = AuthorizationSvc.getTokenFromStore(token);
        User user = datastore.find(User.class).field("token").equal(token).get();

        if(user == null) {
            datastore.delete(newToken);
            throw new AuthorizationException("User not found");
        } else {
            return user.getAccounts();
        }
    }
}

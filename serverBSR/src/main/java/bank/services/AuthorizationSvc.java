package bank.services;

import com.mongodb.DuplicateKeyException;
import bank.commons.DatastoreHandler;
import bank.commons.ParametersValidation;
import bank.errorHandling.AuthorizationException;
import bank.models.Token;
import bank.models.User;
import org.mongodb.morphia.Datastore;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Web service handling all authorization aspects
 */
@WebService
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
public class AuthorizationSvc {

    @Resource
    private WebServiceContext wscontext;

    @WebMethod
    public void register(@WebParam(name = "username") @XmlElement(required=true) String username,
                         @WebParam(name = "password") @XmlElement(required=true) String password,
                         @WebParam(name = "firstname") @XmlElement(required=true) String firstname,
                         @WebParam(name = "lastname") @XmlElement(required=true) String lastname) throws AuthorizationException, ValidationException {
        Map<String, String> parametersMap = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
            put("firstname", firstname);
            put("lastname", lastname);
        }};
        ParametersValidation.validate(parametersMap);

        try{
            Datastore datastore =  DatastoreHandler.getInstance().getDataStore();
            datastore.save(new User(username, password, firstname, lastname));
        } catch(DuplicateKeyException e){
            throw new AuthorizationException("User already registered");
        }
    }

    /**
     * Login method
     * @param username nick given during registration
     * @param password password given during registration
     * @return token string
     * @throws AuthorizationException when credentials are wrong
     * @throws ValidationException when credentials didn't pass validation
     */
    @WebMethod
    public String login(@WebParam(name = "username") @XmlElement(required=true) String username,
                      @WebParam(name = "password") @XmlElement(required=true) String password) throws AuthorizationException, ValidationException {
        Map<String, String> parametersMap = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }};
        ParametersValidation.validate(parametersMap);

        Datastore datastore =  DatastoreHandler.getInstance().getDataStore();
        User user = datastore.find(User.class).field("username").equal(username).get();
        if(user == null || !password.equals(user.getPassword())) {
            throw new AuthorizationException("Wrong username or password");
        } else {
            Token token = new Token();
            user.setToken(token.getToken());
            datastore.save(token);
            datastore.save(user);
            return token.getToken();
        }

    }

    /**
     * Method responsible for logging out the user
     * @throws AuthorizationException when token is missing
     */
   @WebMethod
   public void logout() throws AuthorizationException {
       String token = getTokenFromHeaders(wscontext);
       Datastore datastore = DatastoreHandler.getInstance().getDataStore();
       Token newToken = datastore.find(Token.class).field("token").equal(token).get();
       if(token==null){
           throw new AuthorizationException("User already logged out");
       } else{
           datastore.delete(newToken);
       }
   }
    /**
     * Method responsible for removing the user
     * @throws AuthorizationException when token is missing or user is missing
     */
   @WebMethod
   public void deleteUser() throws AuthorizationException {
        String token = getTokenFromHeaders(wscontext);
        Datastore datastore =  DatastoreHandler.getInstance().getDataStore();
        Token newToken = datastore.find(Token.class).field("token").equal(token).get();
        User user = datastore.find(User.class).field("token").equal(token).get();
        if(newToken != null){
            if(user != null) {
                datastore.delete(user);
                datastore.delete(newToken);
            } else {
                throw new AuthorizationException("User not found");
            }
        } else{
            throw new AuthorizationException("User not authorized");
        }

   }

    /**
     * Util method to obtain token from headers
      * @param wscontext web service context
     * @return token string
     * @throws AuthorizationException if token wasn't found in headers
     */
    public static String getTokenFromHeaders(WebServiceContext wscontext) throws AuthorizationException {
        Map headers = (Map)wscontext.getMessageContext().get(MessageContext.HTTP_REQUEST_HEADERS);
        ArrayList token = (ArrayList)headers.get("token");
        if(token != null) {
              return (String)token.get(0);
        } else {
             throw new AuthorizationException("Token not found");
        }
    }

    /**
     * Util method for getting token object from datastore
     * @param token token string
     * @return token object
     * @throws AuthorizationException when token string is invalid
     */
    public static Token getTokenFromStore(String token) throws AuthorizationException {
        Datastore datastore =  DatastoreHandler.getInstance().getDataStore();
        Token newToken = datastore.find(Token.class).field("token").equal(token).get();
        if(newToken != null) {
            return newToken;
        } else {
            throw new AuthorizationException("Invalid token");
        }
    }

    /**
     * Method fetching currently logged user
     * @return currently logged user
     * @throws AuthorizationException
     */
    @WebMethod
    public User getCurrentUser() throws AuthorizationException {
        Datastore datastore = DatastoreHandler.getInstance().getDataStore();
        String token = AuthorizationSvc.getTokenFromHeaders(wscontext);
        Token newToken = AuthorizationSvc.getTokenFromStore(token);
        User user = datastore.find(User.class).field("token").equal(token).get();
        return user;
    }
}

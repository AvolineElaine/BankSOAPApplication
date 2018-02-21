package bank.services;

import bank.commons.*;
import bank.errorHandling.AuthorizationException;
import bank.errorHandling.OperationException;
import bank.models.Account;
import bank.models.Operation;
import bank.models.Token;
import bank.models.User;
import org.glassfish.jersey.internal.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mongodb.morphia.Datastore;
import sun.misc.IOUtils;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static bank.services.AuthorizationSvc.getTokenFromHeaders;

@WebService
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
public class BankOperationSvc {

    @Resource
    private WebServiceContext wscontext;
    @WebMethod
    public void makeDeposit(@WebParam(name = "amount") @XmlElement(required = true) int amount, @WebParam(name = "destinationAccount") @XmlElement(required = true) String destinationAccountNo) throws
            AuthorizationException,
            OperationException {
        String token = getTokenFromHeaders(wscontext);
        Datastore datastore = DatastoreHandler.getInstance().getDataStore();
        Token newToken = datastore.find(Token.class).field("token").equal(token).get();
        User user = datastore.find(User.class).field("token").equal(token).get();
        Account account = datastore.find(Account.class).field("number").equal(destinationAccountNo).get();
        if(account==null){
            throw new OperationException("Account not found");
        }
        if(user==null){
            datastore.delete(newToken);
            throw new AuthorizationException("User not found");
        } else{
            if(!user.containsAccount(account.getNumber())){
                throw new OperationException("User not authorized");
            }
            else{
                Operation deposit = new Operation(OperationType.DEPOSIT, amount, destinationAccountNo);
                deposit.execute(account);
                //account.setBalance(account.getBalance()+amount);
                //account.addOperation(deposit);
                datastore.save(account);
            }
        }

    }

    @WebMethod
    public void makeWithdraw(@WebParam(name = "amount") @XmlElement(required = true) int amount, @WebParam(name = "destinationAccount") @XmlElement(required = true) String destinationAccountNo) throws
            AuthorizationException,
            OperationException {
        String token = getTokenFromHeaders(wscontext);
        Datastore datastore = DatastoreHandler.getInstance().getDataStore();
        Token newToken = datastore.find(Token.class).field("token").equal(token).get();
        User user = datastore.find(User.class).field("token").equal(token).get();
        Account account = datastore.find(Account.class).field("number").equal(destinationAccountNo).get();
        if(user==null){
            datastore.delete(newToken);
            throw new AuthorizationException("User not found");
        } else{
            if(!user.containsAccount(account.getNumber())){
                throw new OperationException("User not authorized");
            }
            else{
                Operation withdraw = new Operation(OperationType.WITHDRAW, amount, destinationAccountNo);

                withdraw.execute(account);
                if(amount > account.getBalance()){
                    throw new OperationException("Cash not found");
                }else{
                    //account.setBalance(account.getBalance()-amount);
                    //account.addOperation(withdraw);
                    datastore.save(account);
                }
            }
        }
    }

    @WebMethod
    public void makeTransfer(@WebParam(name = "title") @XmlElement(required = true) String title, @WebParam(name = "amount") @XmlElement(required = true) String amount,
                             @WebParam(name = "sourceAccount") @XmlElement(required = true) String sourceAccountNo, @WebParam(name = "destinationAccount") @XmlElement(required = true) String destinationAccountNo) throws
            AuthorizationException,
            OperationException, ValidationException {
        Map<String, String> parametersMap = new HashMap<String, String>() {{
            put("title", title);
            put("amount", amount);
            put("sender account no", sourceAccountNo);
            put("receiver account no", destinationAccountNo);
        }};
        ParametersValidation.validate(parametersMap);
        int intAmount = Integer.parseInt(amount);
        limitTransfer(intAmount);

        String token = getTokenFromHeaders(wscontext);
        Datastore datastore = DatastoreHandler.getInstance().getDataStore();
        Token newToken = datastore.find(Token.class).field("token").equal(token).get();
        if(newToken==null){
            throw new AuthorizationException("Token not found");
        }
        User user = datastore.find(User.class).field("token").equal(token).get();

        Account sourceAccount = datastore.find(Account.class).field("number").equal(sourceAccountNo).get();

        if (sourceAccount == null) {
            throw new OperationException("Source bank account does not exist");
        }
        if(user==null){
            throw new AuthorizationException("User not found");
        }
        if(!user.containsAccount(sourceAccountNo)){
            throw new OperationException("User not authorized");
        }
        if(sourceAccountNo.equals(destinationAccountNo)) {
            throw new OperationException("Destination is equal to source");
        }

        //Operation transfer = new Operation(OperationType.TRANSFER, intAmount, title, sourceAccountNo, destinationAccountNo, user.getUsername());
        //transfer.execute(datastore);


        Operation outTransfer = new Operation(OperationType.OUTTRANSFER, intAmount, title, sourceAccountNo, destinationAccountNo, user.getFirstname()+' '+user.getLastname());

        if (destinationAccountNo.substring(2, 10).equals(Constants.ID)) {
            Account destinationAccount = datastore.find(Account.class).field("number").equal(destinationAccountNo).get();
            if (destinationAccount == null) {
                throw new OperationException("Target bank account does not exist");
            }
            Operation inTransfer = new Operation(OperationType.INTRANSFER, intAmount, title, sourceAccountNo, destinationAccountNo, user.getFirstname()+' '+user.getLastname());
            makeInternalTransfer(datastore, sourceAccount, destinationAccount, inTransfer, outTransfer);
        } else {
            try {
                makeExternalTransfer(datastore, sourceAccount, outTransfer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void limitTransfer(int amount) throws OperationException {
        if (amount > 1000000) {
            throw new OperationException("Sum is higher than 1 000 000");
        }
    }

    private void makeInternalTransfer(Datastore datastore, Account sourceBankAccount, Account targetBankAccount,
                                      Operation inTransfer, Operation outTransfer) throws OperationException {
        outTransfer.execute(sourceBankAccount);
        inTransfer.execute(targetBankAccount);

        datastore.save(sourceBankAccount);
        datastore.save(targetBankAccount);
    }

    private void makeExternalTransfer(Datastore datastore, Account sourceBankAccount, Operation outTransfer)
            throws OperationException, IOException {
        String bankNo = outTransfer.getDestinationAccountNo().substring(2, 10);
        Map<String, String> bankToIpMap = IpMapper.getInstance().getBankToIpMap();
        if (!bankToIpMap.containsKey(bankNo)) {
            throw new OperationException("Unknown bank of target account");
        }

        outTransfer.execute(sourceBankAccount);

        String charset = "UTF-8";
        String url = bankToIpMap.get(bankNo) + "/transfer";
        String data = "{" +
                "\"amount\":" + (int) (outTransfer.getAmount() * 100) + "," +
                "\"sender_account\":" + "\"" + outTransfer.getSourceAccountNo() + "\"," +
                "\"receiver_account\":" + "\"" + outTransfer.getDestinationAccountNo() + "\"," +
                "\"title\":" + "\"" + outTransfer.getTitle() + "\"}";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
        connection.setRequestProperty("Authorization", "Basic " +
                Base64.encodeAsString(Constants.BANK_USERNAME + ":" + Constants.BANK_PASSWORD));

        OutputStream requestBody = connection.getOutputStream();
        requestBody.write(data.getBytes(charset));
        requestBody.close();
        connection.connect();

        int status = connection.getResponseCode();
        if (status != 201) {
            InputStream response = connection.getErrorStream();
            if (response != null) {
                String message = new String(IOUtils.readFully(response, -1, true));
                JSONParser parser = new JSONParser();
                JSONObject obj = null;
                try {
                    obj = (JSONObject) parser.parse(message);
                    if (obj.containsKey("error")) {
                        throw new OperationException((String) obj.get("error"));
                    } else {
                        throw new OperationException("Unknown error occurs");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                throw new OperationException("Unknown error occurs");
            }
        } else {
            datastore.save(sourceBankAccount);
        }
    }
}

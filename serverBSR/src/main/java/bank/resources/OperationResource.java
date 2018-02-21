package bank.resources;

import bank.commons.DatastoreHandler;
import bank.errorHandling.OperationException;
import bank.models.Account;
import bank.models.Operation;
import org.mongodb.morphia.Datastore;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Class handling reception of external transfer to this account
 */

@Path("/operation")
public class OperationResource {
    /**
     * Receives external transfer
     * @param extTransfer object representing transfer that is serialized from JSON content
     * @return response status 201
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response makeTransfer(@NotNull Operation extTransfer) {
        validateTransfer(extTransfer);

        Datastore datastore = DatastoreHandler.getInstance().getDataStore();
        Account targetBankAccount = datastore.find(Account.class).field("number")
                .equal(extTransfer.getDestinationAccountNo()).get();
        if (targetBankAccount == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("{\n" +
                    "  \"error\": \"target bank account not exists\"\n" +
                    "}").build());
        }

        try {
            extTransfer.execute(targetBankAccount);
        } catch (OperationException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
                    "  \"error\": \"unknown error\"\n" +
                    "}").build());
        }
        datastore.save(targetBankAccount);

        return Response.created(null).build();
    }

    private void validateTransfer(Operation extTransfer) {
        String errorMessage = "";

        if (extTransfer.getAmount() <= 0.0 || extTransfer.getAmount() > 1000000) {
            errorMessage += "amount,";
        }
        if (extTransfer.getTitle() == null || extTransfer.getTitle().length() == 0) {
            errorMessage += "title,";
        }
        if (extTransfer.getSourceAccountNo() == null || !extTransfer.getSourceAccountNo().matches("[0-9]+") ||
                extTransfer.getSourceAccountNo().length() != 26){ //||
                //!Account.validateCheckSum(transfer.getSourceAccountNo())) {
            errorMessage += "sender_account,";
        }
        if (extTransfer.getDestinationAccountNo() == null ||
                !(extTransfer.getDestinationAccountNo().matches("[0-9]+") && extTransfer.getDestinationAccountNo().length() == 26)) {
            errorMessage += "receiver_account,";
        }

        if (errorMessage.length() > 0) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            errorMessage += " is missing or invalid";
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
                    "  \"error\": \"" + errorMessage + "\"\n" +
                    "}").build());
        }

        if (extTransfer.getSourceAccountNo().equals(extTransfer.getDestinationAccountNo())) {
            errorMessage = "sender_account is the same as receiver_account";
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("{\n" +
                    "  \"error\": \"" + errorMessage + "\"\n" +
                    "}").build());
        }
    }
}

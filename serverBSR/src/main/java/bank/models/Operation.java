package bank.models;

import bank.commons.OperationType;
import bank.errorHandling.OperationException;
import org.mongodb.morphia.annotations.Embedded;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Model representing bank operation
 */

@Embedded
@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Operation {
    @NotNull
    private int amount;
    private String title;
    @NotNull
    private Date date;
    private String sourceAccountNo;
    @NotNull
    private String destinationAccountNo;
    @NotNull
    private OperationType type;
    private String senderName;


    public Operation(){}

    public Operation(OperationType type, int amount, String title, String sourceAccountNo, String destinationAccountNo, String senderName){
        this.type = type;
        this.amount = amount;
        this.title = title;
        this.sourceAccountNo = sourceAccountNo;
        this.destinationAccountNo = destinationAccountNo;
        this.senderName = senderName;

        this.date = new Date();
    }

    public Operation(OperationType type, int amount, String destinationAccountNo){
        this.type = type;
        this.amount = amount;
        this.destinationAccountNo = destinationAccountNo;
        this.date = new Date();
        this.sourceAccountNo = null;
        this.senderName = null;
    }

    public void execute(Account account) throws OperationException {
        if(amount <= 0) {
            throw new OperationException("Wrong amount");
        }

        if (type == OperationType.INTRANSFER || type == OperationType.DEPOSIT) {
            account.setBalance(account.getBalance() + amount);
        } else {
            if (amount > account.getBalance()) {
                throw new OperationException("Amount bigger than current account balance");
            }

            account.setBalance(account.getBalance() - amount);
        }
        account.addOperation(this);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getDestinationAccountNo() {
        return destinationAccountNo;
    }

    public void setDestinationAccountNo(String destinationAccountNo) {
        this.destinationAccountNo = destinationAccountNo;
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
    }
}

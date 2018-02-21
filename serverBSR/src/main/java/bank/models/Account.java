package bank.models;

import bank.commons.Constants;
import bank.commons.DatastoreHandler;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;

/**
 * Model representing bank account
 */
@Entity("accounts")
public class Account {
    @Id
    @XmlTransient
    private ObjectId id;

    private ArrayList<Operation> history;

    @NotNull
    @Indexed(name = "number", unique = true)
    private String number;

    @NotNull
    private int balance;

    @NotNull
    private String ownerName;

    public Account(){
        if (history == null) {
            history = new ArrayList<>();
        }
    }

    public Account(String ownerName){
        this.history = new ArrayList<>();
        this.balance = 0;
        this.number = generateNumber();
        this.ownerName = ownerName;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @XmlElementWrapper(name="history")
    @XmlElementRef()
    public ArrayList<Operation> getHistory() {
        return history;
    }

    public void addOperation(Operation operation){
        history.add(operation);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    private String generateNumber() {
        Datastore datastore = DatastoreHandler.getInstance().getDataStore();
        Query<Sequence> query = datastore.find(Sequence.class, "id", "accountNoSequence");
        UpdateOperations<Sequence> operation = datastore.createUpdateOperations(Sequence.class).inc("seq");
        long count = datastore.findAndModify(query, operation).getSeq();
        String accountNo = Constants.ID + String.format("%016d", count);
        String tmpNo = accountNo + "252100";
        String part1 = tmpNo.substring(0,15);
        String part2 = tmpNo.substring(15);
        long rest1 = Long.parseLong(part1)%97;
        long rest2 = Long.parseLong(rest1 + part2)%97;
        long checkSum = 98 - rest2;
        accountNo = String.format("%02d", checkSum) + accountNo;
        return accountNo;
    }

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
}

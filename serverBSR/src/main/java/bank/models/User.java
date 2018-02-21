package bank.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Model representing bank user
 */

@Entity("users")
public class User {

    @Id
    @XmlTransient
    private ObjectId id;
    @NotNull
    @Indexed(name = "username", unique = true)
    private String username;
    @NotNull
    @XmlTransient
    private String password;
    @NotNull
    @XmlTransient
    private String firstname;
    @NotNull
    @XmlTransient
    private String lastname;

    @XmlTransient
    private String token;

    @Reference
    private ArrayList<Account> accounts;

    public User(){}

    public User(String username, String password, String firstname, String lastname){
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public boolean containsAccount(String accountNo) {
        return accounts.stream().filter(bankAccount -> bankAccount.getNumber().equals(accountNo)).count() > 0;
    }

    public void removeBankAccount(String accountNo) {
        Account bankAccount = accounts.stream()
                .filter(account -> account.getNumber().equals(accountNo))
                .collect(Collectors.toList())
                .get(0);
        accounts.remove(bankAccount);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}

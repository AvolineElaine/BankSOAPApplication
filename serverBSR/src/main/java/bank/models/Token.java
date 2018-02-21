package bank.models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import java.security.SecureRandom;

/**
 * Model representing token given to user after authorization procedure
 */

@Entity("tokens")
public class Token {
    @Id
    private String id;

    @Indexed(name = "token", unique = true)
    private String token;


    public Token(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

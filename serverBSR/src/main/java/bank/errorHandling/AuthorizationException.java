package bank.errorHandling;

/**
 * Exception thrown during login, register or token operations
 */
public class AuthorizationException extends Exception{
    public AuthorizationException(String message) {
        super(message);
    }
}

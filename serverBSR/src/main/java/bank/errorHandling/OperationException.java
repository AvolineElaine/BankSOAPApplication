package bank.errorHandling;

/**
 * Exception thrown during executing bank operations
 */
public class OperationException extends Exception{
    public OperationException(String message) {
        super(message);
    }
}

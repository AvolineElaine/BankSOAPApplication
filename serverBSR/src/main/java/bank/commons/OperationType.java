package bank.commons;


/**
 * Types of operations available to execute in the bank
 */
public enum OperationType {
    /**
     * transfer to the account
     */
    INTRANSFER,

    /**
     * transfer from the account
     */
    OUTTRANSFER,

    /**
     * withdrawal from account
     */
    WITHDRAW,

    /**
     * deposit into account
     */
    DEPOSIT;
}

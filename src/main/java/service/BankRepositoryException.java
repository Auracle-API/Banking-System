package service;

public class BankRepositoryException extends RuntimeException {
    public BankRepositoryException(String message) {
        super(message);
    }
    public BankRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    public BankRepositoryException(){
        super();
    }
}

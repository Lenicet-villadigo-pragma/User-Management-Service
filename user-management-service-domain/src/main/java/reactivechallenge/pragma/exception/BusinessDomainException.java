package reactivechallenge.pragma.exception;

public class BusinessDomainException extends RuntimeException {
    public BusinessDomainException(String message) {
        super(message);
    }
}

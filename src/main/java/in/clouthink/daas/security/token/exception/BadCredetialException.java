package in.clouthink.daas.security.token.exception;

public class BadCredetialException extends RuntimeException {
    
    public BadCredetialException() {
    }
    
    public BadCredetialException(String message) {
        super(message);
    }
    
    public BadCredetialException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BadCredetialException(Throwable cause) {
        super(cause);
    }
}

package in.clouthink.daas.security.token.exception;

public class BadCredentialException extends RuntimeException {
    
    public BadCredentialException() {
    }
    
    public BadCredentialException(String message) {
        super(message);
    }
    
    public BadCredentialException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BadCredentialException(Throwable cause) {
        super(cause);
    }
}

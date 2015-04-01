package in.clouthink.daas.security.token.exception;

public class AuthorizationFailureException extends RuntimeException {
    
    public AuthorizationFailureException() {
    }
    
    public AuthorizationFailureException(String message) {
        super(message);
    }
    
    public AuthorizationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AuthorizationFailureException(Throwable cause) {
        super(cause);
    }
}

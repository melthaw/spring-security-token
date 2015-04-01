package in.clouthink.daas.security.token.exception;

public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException() {
    }

    public AuthenticationFailureException(String message) {
        super(message);
    }

    public AuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailureException(Throwable cause) {
        super(cause);
    }
}

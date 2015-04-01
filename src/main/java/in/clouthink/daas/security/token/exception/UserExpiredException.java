package in.clouthink.daas.security.token.exception;

public class UserExpiredException extends RuntimeException {

    public UserExpiredException() {
    }

    public UserExpiredException(String message) {
        super(message);
    }

    public UserExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExpiredException(Throwable cause) {
        super(cause);
    }
}

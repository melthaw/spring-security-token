package in.clouthink.daas.security.token.exception;

public class UserLockedException extends RuntimeException {

    public UserLockedException() {
    }

    public UserLockedException(String message) {
        super(message);
    }

    public UserLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLockedException(Throwable cause) {
        super(cause);
    }
}

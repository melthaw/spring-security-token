package in.clouthink.daas.security.token.exception;

/**
 * @since 1.7.0
 */
public class LoginLockedException extends BadCredentialException {

    private short attempts;

    public LoginLockedException(short attempts) {
        this.attempts = attempts;
    }

    public LoginLockedException(short attempts, String message) {
        super(message);
        this.attempts = attempts;
    }

    public LoginLockedException(short attempts, String message, Throwable cause) {
        super(message, cause);
        this.attempts = attempts;
    }

    public LoginLockedException(short attempts, Throwable cause) {
        super(cause);
        this.attempts = attempts;
    }

    public short getAttempts() {
        return attempts;
    }

}

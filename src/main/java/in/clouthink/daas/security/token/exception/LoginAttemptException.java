package in.clouthink.daas.security.token.exception;

public class LoginAttemptException extends BadCredentialException {

    private final short attempts;

    private final short maxAttempts;

    private final long attemptTimeout;

    public LoginAttemptException(short attempts, short maxAttempts, long attemptTimeout) {
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.attemptTimeout = attemptTimeout;
    }

    public LoginAttemptException(short attempts, short maxAttempts, long attemptTimeout, String message) {
        super(message);
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.attemptTimeout = attemptTimeout;
    }

    public LoginAttemptException(short attempts, short maxAttempts, long attemptTimeout, String message,
                                 Throwable cause) {
        super(message, cause);
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.attemptTimeout = attemptTimeout;
    }

    public LoginAttemptException(short attempts, short maxAttempts, long attemptTimeout, Throwable cause) {
        super(cause);
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.attemptTimeout = attemptTimeout;
    }

    public short getAttempts() {
        return attempts;
    }

    public short getMaxAttempts() {
        return maxAttempts;
    }

    public long getAttemptTimeout() {
        return attemptTimeout;
    }
}

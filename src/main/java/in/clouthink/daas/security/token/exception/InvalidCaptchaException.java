package in.clouthink.daas.security.token.exception;

/**
 * Throw this when captcha is invalid
 *
 * @since 1.8.0
 */
public class InvalidCaptchaException extends RuntimeException {

    public InvalidCaptchaException() {
    }

    public InvalidCaptchaException(String message) {
        super(message);
    }

    public InvalidCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCaptchaException(Throwable cause) {
        super(cause);
    }

}

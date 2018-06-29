package in.clouthink.daas.security.token.exception;

/**
 * Throw this when captcha is invalid
 *
 * @since 1.8.0
 */
public class IncorrectCaptchaException extends RuntimeException {

    public IncorrectCaptchaException() {
    }

    public IncorrectCaptchaException(String message) {
        super(message);
    }

    public IncorrectCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectCaptchaException(Throwable cause) {
        super(cause);
    }

}

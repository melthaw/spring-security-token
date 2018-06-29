package in.clouthink.daas.security.token.exception;

/**
 * Throw this when captcha not found or captcha is expired.
 *
 * @since 1.8.0
 */
public class CaptchaExpiredException extends RuntimeException {

    public CaptchaExpiredException() {
    }

    public CaptchaExpiredException(String message) {
        super(message);
    }

    public CaptchaExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaExpiredException(Throwable cause) {
        super(cause);
    }

}

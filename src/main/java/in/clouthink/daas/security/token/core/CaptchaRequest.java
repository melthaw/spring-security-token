package in.clouthink.daas.security.token.core;

/**
 * The captcha request def
 *
 * @auther dz
 * @since 1.8.0
 */
public interface CaptchaRequest {

    String getCaptchaId();

    String getCaptchaResponse();

}

package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.exception.IncorrectCaptchaException;

/**
 * The manager to create and verify captcha
 *
 * @since 1.8.0
 */
public interface CaptchaManager extends CaptchaOptions {

    /**
     * @return Captcha generate in random
     */
    Captcha generate();

    /**
     * @param request
     * @throws IncorrectCaptchaException if the captcha response is wrong
     * @throws in.clouthink.daas.security.token.exception.CaptchaExpiredException if the captcha is not found
     */
    void verify(CaptchaVerifyRequest request);

}

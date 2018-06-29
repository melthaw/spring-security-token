package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Captcha;

/**
 * crud for captcha
 *
 * @since 1.8.0
 */
public interface CaptchaProvider<T extends Captcha> {

    void saveCaptcha(T captcha);

    T findCaptcha(String captchaId);

    void revokeCaptcha(T captcha);

}

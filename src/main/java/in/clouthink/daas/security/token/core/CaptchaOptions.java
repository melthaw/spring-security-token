package in.clouthink.daas.security.token.core;

import java.io.Serializable;

/**
 * The captcha options to control how to generate the captcha.
 *
 * @author dz
 * @since 1.8.0
 */
public interface CaptchaOptions extends Serializable {

    /**
     * @param length the length of the captcha, must be the range of 4~12
     */
    void setLength(int length);

    /**
     * Enable to generate the number captcha
     */
    void enableNumber();

    /**
     * Enable to generate the char captcha
     */
    void enableChar();

    /**
     * Disable to generate the number captcha
     */
    void disableNumber();

    /**
     * Disable to generate the char captcha
     */
    void disableChar();

    /**
     * @param timeout milli seconds , must be the range of 30s ~ 10*60s
     */
    void setCaptchaTimeout(long timeout);

}

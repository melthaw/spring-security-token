package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.core.captcha.RandomUtils;
import in.clouthink.daas.security.token.exception.CaptchaExpiredException;
import in.clouthink.daas.security.token.exception.IncorrectCaptchaException;
import in.clouthink.daas.security.token.spi.CaptchaProvider;
import in.clouthink.daas.security.token.spi.impl.model.CaptchaEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The default captcha mgr impl
 *
 * @since 1.8.0
 */
public class DefaultCaptchaManager implements CaptchaManager, InitializingBean {

    int length = 4;

    boolean numberEnabled = true;

    boolean charEnabled = true;

    boolean caseSensitive = false;

    long captchaTimeout = 60 * 1000;

    private CaptchaProvider<Captcha> captchaProvider;

    public void setCaptchaProvider(CaptchaProvider captchaProvider) {
        this.captchaProvider = captchaProvider;
    }

    @Override
    public void setLength(int length) {
        if (length < 4 || length > 12) {
            throw new IllegalArgumentException();
        }
        this.length = length;
    }

    @Override
    public void enableNumber() {
        this.numberEnabled = true;
    }

    @Override
    public void enableChar() {
        this.charEnabled = true;
    }

    @Override
    public void disableNumber() {
        this.numberEnabled = false;
        this.charEnabled = true;
    }

    @Override
    public void disableChar() {
        this.charEnabled = false;
        this.numberEnabled = true;
    }

    @Override
    public void enableCaseSensitive() {
        this.caseSensitive = true;
    }

    @Override
    public void disableCaseSensitive() {
        this.caseSensitive = false;
    }

    @Override
    public void setCaptchaTimeout(long timeout) {
        if (timeout < 30 * 1000 || timeout > 10 * 60 * 1000) {
            throw new IllegalArgumentException();
        }
        this.captchaTimeout = timeout;
    }

    @Override
    public Captcha generate() {
        String value = "";
        if (this.charEnabled && this.numberEnabled) {
            value = RandomUtils.generateMixString(this.length);
        }
        else if (this.charEnabled) {
            value = RandomUtils.generateLetterString(this.length);
        }
        else if (this.numberEnabled) {
            value = RandomUtils.generateNumberString(this.length);
        }

        Captcha captcha = CaptchaEntity.create(value, this.captchaTimeout);
        captchaProvider.saveCaptcha(captcha);
        return captcha;
    }

    @Override
    public void verify(CaptchaVerifyRequest request) {
        Captcha captcha = captchaProvider.findCaptcha(request.getCaptchaId());
        if (captcha == null) {
            throw new CaptchaExpiredException();
        }
        if (this.caseSensitive) {
            if (!captcha.getValue().equals(request.getCaptchaResponse())) {
                throw new IncorrectCaptchaException();
            }
        }
        else {
            if (!captcha.getValue().equalsIgnoreCase(request.getCaptchaResponse())) {
                throw new IncorrectCaptchaException();
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(captchaProvider);
    }
}

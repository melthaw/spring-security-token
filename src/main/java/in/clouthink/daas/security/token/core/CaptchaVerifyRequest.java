package in.clouthink.daas.security.token.core;

/**
 * The captcha verify request
 *
 * @since 1.8.0
 */
public class CaptchaVerifyRequest implements CaptchaRequest {

    private String captchaId;

    private String captchaResponse;

    public CaptchaVerifyRequest(
            String captchaId,
            String captchaResponse) {
        this.captchaId = captchaId;
        this.captchaResponse = captchaResponse;
    }

    @Override
    public String getCaptchaId() {
        return captchaId;
    }

    @Override
    public String getCaptchaResponse() {
        return captchaResponse;
    }

}

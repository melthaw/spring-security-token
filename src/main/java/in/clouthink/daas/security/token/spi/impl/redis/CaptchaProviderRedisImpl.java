package in.clouthink.daas.security.token.spi.impl.redis;

import in.clouthink.daas.security.token.core.Captcha;
import in.clouthink.daas.security.token.spi.CaptchaProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * The redis crud for captcha.
 *
 * @since 1.8.0
 */
public class CaptchaProviderRedisImpl implements CaptchaProvider<Captcha> {

    public static final Log logger = LogFactory.getLog(CaptchaProviderRedisImpl.class);

    @Autowired
    private RedisTemplate<String, Captcha> captchaRedisTemplate;

    @Override
    public void saveCaptcha(Captcha captcha) {
        logger.debug(String.format("Put CAPTCHA:%s expiredAt:%s",
                                   captcha.getValue(),
                                   captcha.getExpiredTime()));
        // put the captcha to cache
        captchaRedisTemplate.opsForHash().put("CAPTCHA:" + captcha.getId(),
                                              captcha.getId(),
                                              captcha);
        captchaRedisTemplate.expireAt("CAPTCHA:" + captcha.getId(),
                                      captcha.getExpiredTime());

    }

    @Override
    public Captcha findCaptcha(String captchaId) {
        logger.debug(String.format("Get CAPTCHA:%s", captchaId));
        return (Captcha) captchaRedisTemplate.opsForHash().get("CAPTCHA:" + captchaId, captchaId);
    }

    @Override
    public void revokeCaptcha(Captcha captcha) {
        logger.debug(String.format("Del CAPTCHA:%s", captcha));
        if (captcha == null) {
            return;
        }
        captchaRedisTemplate.opsForHash().delete("CAPTCHA:" + captcha.getId(),
                                                 captcha.getId());
    }

}

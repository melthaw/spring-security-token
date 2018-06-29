package in.clouthink.daas.security.token.spi.impl.memory;

import in.clouthink.daas.security.token.core.Captcha;
import in.clouthink.daas.security.token.spi.CaptchaProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple store the captcha in memory.
 *
 * @since 1.8.0
 */
public class CaptchaProviderMemoryImpl implements CaptchaProvider<Captcha> {

    public static final Log logger = LogFactory.getLog(CaptchaProviderMemoryImpl.class);

    private Map<String, Captcha> captchaMap = new ConcurrentHashMap<String, Captcha>();

    @Override
    public void saveCaptcha(Captcha captcha) {
        logger.debug(String.format("Put CAPTCHA_%s expiredAt:%s",
                                   captcha.getId(),
                                   captcha.getExpiredTime()));
        captchaMap.put("CAPTCHA_" + captcha.getId(), captcha);
    }

    @Override
    public Captcha findCaptcha(String id) {
        logger.debug(String.format("Get CAPTCHA_%s", id));
        return captchaMap.get("CAPTCHA_" + id);
    }

    @Override
    public void revokeCaptcha(Captcha captcha) {
        logger.debug(String.format("Del CAPTCHA_%s", captcha));
        if (captcha == null) {
            return;
        }
        captchaMap.remove("CAPTCHA_" + captcha.getId());
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void cleanExpiredCaptcha() {
        logger.debug("Start to clean the expired captcha automatically.");
        Set<Captcha> captchaSet = new HashSet<Captcha>();
        for (Captcha captcha : captchaMap.values()) {
            if (captcha.getExpiredTime().getTime() < System.currentTimeMillis()) {
                captchaSet.add(captcha);
            }
        }
        for (Captcha captcha : captchaSet) {
            captchaMap.remove("CAPTCHA_" + captcha.getId());
        }
    }

}

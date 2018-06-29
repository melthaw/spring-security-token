package in.clouthink.daas.security.token.spi.impl.mongodb;

import in.clouthink.daas.security.token.spi.CaptchaProvider;
import in.clouthink.daas.security.token.spi.impl.model.CaptchaEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The mongodb crud for captcha.
 *
 * @since 1.8.0
 */
public class CaptchaProviderMongodbImpl implements CaptchaProvider<CaptchaEntity> {

    public static final Log logger = LogFactory.getLog(CaptchaProviderMongodbImpl.class);

    @Autowired
    private CaptchaEntityRepository captchaEntityRepository;

    @Override
    public void saveCaptcha(CaptchaEntity captcha) {
        logger.debug(String.format("Put captcha:%s expiredAt:%s",
                                   captcha.getId(),
                                   captcha.getExpiredTime()));
        if (captcha == null) {
            return;
        }
        captchaEntityRepository.save(captcha);
    }

    @Override
    public CaptchaEntity findCaptcha(String id) {
        logger.debug(String.format("Get captcha:%s", id));
        return captchaEntityRepository.findOne(id);
    }

    @Override
    public void revokeCaptcha(CaptchaEntity captcha) {
        logger.debug(String.format("Del captcha:%s", captcha));
        if (captcha == null) {
            return;
        }
        captchaEntityRepository.delete(captcha);
    }

}

package in.clouthink.daas.security.token.core;

import java.io.Serializable;
import java.util.Date;

/**
 * The captcha def.
 *
 * @author dz
 * @since 1.8.0
 */
public interface Captcha extends Serializable {

    /**
     * @return the id of captcha
     */
    String getId();

    /**
     * @return the value of captcha
     */
    String getValue();

    /**
     * @return the expired time of captcha
     */
    Date getExpiredTime();

}

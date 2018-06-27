package in.clouthink.daas.security.token.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dz
 * @since 1.7.0
 */
public interface LoginAttempt extends Serializable {

    /**
     * @return the user to do attempt login
     */
    String getUsername();

    /**
     * @return how many times the user attempt login (goes failure)
     */
    short getAttempts();

    /**
     * @return the expired time of login attempt (it's reset after reach the expired time)
     */
    Date getExpiredTime();

    /**
     * @return the latest active time of token (user accessed the protected
     * resource with the token)
     */
    Date getLatestTime();

    /**
     * increase attempt & update the latest time to current time
     */
    void increaseAttempt();

}

package in.clouthink.daas.security.token.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dz
 * @since 1.7.0
 */
public interface LoginAttempt extends Serializable {

    String getUsername();

    int getAttemptCount();

    int getAttemptLimit();

    Date getLatestAttemptTime();

    void reset();

}

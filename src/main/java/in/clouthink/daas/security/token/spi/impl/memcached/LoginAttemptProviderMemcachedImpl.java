package in.clouthink.daas.security.token.spi.impl.memcached;

import in.clouthink.daas.security.token.core.LoginAttempt;
import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;
import in.clouthink.daas.security.token.spi.LoginAttemptProvider;
import in.clouthink.daas.security.token.spi.TokenProvider;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * The memcached crud for login attempt.
 *
 * @since 1.7.0
 */
public class LoginAttemptProviderMemcachedImpl implements LoginAttemptProvider<LoginAttempt> {

    public static final Log logger = LogFactory.getLog(LoginAttemptProviderMemcachedImpl.class);

    @Autowired
    private MemcachedClient memcachedClient;

    @Override
    public void saveLoginAttempt(LoginAttempt loginAttempt) {
        logger.debug(String.format("Put loginAttempt:%s expiredAt:%s",
                                   loginAttempt.getUsername(),
                                   loginAttempt.getExpiredTime()));
        memcachedClient.add("LA_" + loginAttempt.getUsername(),
                            (int) ((loginAttempt.getExpiredTime().getTime() - System.currentTimeMillis()) / 1000),
                            loginAttempt);
    }

    @Override
    public LoginAttempt findByUsername(String username) {
        logger.debug(String.format("Get loginAttempt:%s", username));
        return (LoginAttempt) memcachedClient.get("LA_" + username);
    }

    @Override
    public void revokeLoginAttempt(LoginAttempt loginAttempt) {
        logger.debug(String.format("Del loginAttempt:%s", loginAttempt));
        if (loginAttempt == null) {
            return;
        }
        memcachedClient.delete("LA_" + loginAttempt.getUsername());
    }

}

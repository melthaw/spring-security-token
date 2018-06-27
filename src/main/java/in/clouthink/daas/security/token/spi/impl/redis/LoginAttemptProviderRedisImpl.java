package in.clouthink.daas.security.token.spi.impl.redis;

import in.clouthink.daas.security.token.core.LoginAttempt;
import in.clouthink.daas.security.token.spi.LoginAttemptProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * The redis crud for login attempt.
 *
 * @since 1.7.0
 */
public class LoginAttemptProviderRedisImpl implements LoginAttemptProvider<LoginAttempt> {

    public static final Log logger = LogFactory.getLog(LoginAttemptProviderRedisImpl.class);

    @Autowired
    private RedisTemplate<String, LoginAttempt> loginAttemptRedisTemplate;

    @Override
    public void saveLoginAttempt(LoginAttempt loginAttempt) {
        logger.debug(String.format("Put LA_%s expiredAt:%s",
                                   loginAttempt.getUsername(),
                                   loginAttempt.getExpiredTime()));

        // put the token to cache
        loginAttemptRedisTemplate.opsForHash().put("LA_" + loginAttempt.getUsername(),
                                                   loginAttempt.getUsername(),
                                                   loginAttempt);
        loginAttemptRedisTemplate.expireAt("LA_" + loginAttempt.getUsername(),
                                           loginAttempt.getExpiredTime());

    }

    @Override
    public LoginAttempt findByUsername(String username) {
        logger.debug(String.format("Get LA_%s", username));
        return (LoginAttempt) loginAttemptRedisTemplate.opsForHash().get("LA_" + username, username);
    }

    @Override
    public void revokeLoginAttempt(LoginAttempt loginAttempt) {
        logger.debug(String.format("Del LA_%s", loginAttempt));
        if (loginAttempt == null) {
            return;
        }
        loginAttemptRedisTemplate.opsForHash().delete("LA_" + loginAttempt.getUsername(),
                                                      loginAttempt.getUsername());
    }

}

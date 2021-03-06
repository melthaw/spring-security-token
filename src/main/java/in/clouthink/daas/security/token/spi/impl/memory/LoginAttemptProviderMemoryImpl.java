package in.clouthink.daas.security.token.spi.impl.memory;

import in.clouthink.daas.security.token.core.LoginAttempt;
import in.clouthink.daas.security.token.spi.LoginAttemptProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple store the login attempt in memory.
 *
 * @since 1.7.0
 */
public class LoginAttemptProviderMemoryImpl implements LoginAttemptProvider<LoginAttempt> {

    public static final Log logger = LogFactory.getLog(LoginAttemptProviderMemoryImpl.class);

    private Map<String, LoginAttempt> loginAttemptMap = new ConcurrentHashMap<String, LoginAttempt>();

    @Override
    public void saveLoginAttempt(LoginAttempt loginAttempt) {
        logger.debug(String.format("Put LA_%s expiredAt:%s",
                                   loginAttempt.getUsername(),
                                   loginAttempt.getExpiredTime()));
        loginAttemptMap.put("LA_" + loginAttempt.getUsername(), loginAttempt);
    }

    @Override
    public LoginAttempt findByUsername(String username) {
        logger.debug(String.format("Get LA_%s", username));
        return loginAttemptMap.get("LA_" + username);
    }

    @Override
    public void revokeLoginAttempt(LoginAttempt loginAttempt) {
        logger.debug(String.format("Del LA_%s", loginAttempt));
        if (loginAttempt == null) {
            return;
        }
        loginAttemptMap.remove("LA_" + loginAttempt.getUsername());
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void cleanExpiredLoginAttempt() {
        logger.debug("Start to clean the expired login attempts automatically.");
        Set<LoginAttempt> loginAttempts = new HashSet<LoginAttempt>();
        for (LoginAttempt loginAttempt : loginAttemptMap.values()) {
            if (loginAttempt.getExpiredTime().getTime() < System.currentTimeMillis()) {
                loginAttempts.add(loginAttempt);
            }
        }
        for (LoginAttempt loginAttempt : loginAttempts) {
            loginAttemptMap.remove("LA_" + loginAttempt.getUsername());
        }
    }

}

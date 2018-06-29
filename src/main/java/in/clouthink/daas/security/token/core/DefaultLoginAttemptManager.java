package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.spi.LoginAttemptProvider;
import in.clouthink.daas.security.token.spi.impl.model.LoginAttemptEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The default impl for LoginAttemptManager
 *
 * @author dz
 * @since 1.7.0
 */
public class DefaultLoginAttemptManager implements LoginAttemptManager, InitializingBean {

    /**
     * The provider to store the LoginAttempt
     */
    private LoginAttemptProvider<LoginAttempt> loginAttemptProvider;

    /**
     * The user will be locked if user try to login failed in maxAttempts times.
     * <p>
     * The default times is 5.
     */
    private short maxAttempts = 5;

    /**
     * If user try to login failed , the failure is counted . And it will be cleared or reset when timeout.
     * <p>
     * The default duration is 1 day.
     */
    private long attemptTimeout = 24 * 60 * 60 * 1000;

    public DefaultLoginAttemptManager() {
    }

    public void setLoginAttemptProvider(LoginAttemptProvider loginAttemptProvider) {
        this.loginAttemptProvider = loginAttemptProvider;
    }

    @Override
    public short getMaxAttempts() {
        return maxAttempts;
    }

    @Override
    public long getAttemptTimeout() {
        return attemptTimeout;
    }

    @Override
    public void setMaxAttempts(short maxAttempts) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("The max attempt count can't be less than 1");
        }
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void setAttemptTimeout(long loginAttemptTimeout) {
        if (loginAttemptTimeout < 60 * 1000) {
            throw new IllegalArgumentException("The login attempt timeout can't be less than 1 minutes");
        }
        this.attemptTimeout = loginAttemptTimeout;
    }

    @Override
    public LoginAttempt create(String username) {
        LoginAttempt loginAttempt = loginAttemptProvider.findByUsername(username);
        if (loginAttempt != null) {
            return loginAttempt;
        }

        loginAttempt = LoginAttemptEntity.create(username, this.attemptTimeout);
        loginAttemptProvider.saveLoginAttempt(loginAttempt);
        return loginAttempt;
    }

    @Override
    public LoginAttempt findByUsername(String username) {
        LoginAttempt result = loginAttemptProvider.findByUsername(username);
        if (result == null) {
            return null;
        }
        // auto revoke the token if the token is expired
        if (result.getExpiredTime().getTime() < System.currentTimeMillis()) {
            loginAttemptProvider.revokeLoginAttempt(result);
            return null;
        }
        return result;
    }

    @Override
    public void reset(String username) {
        LoginAttempt result = loginAttemptProvider.findByUsername(username);
        if (result == null) {
            return;
        }

        loginAttemptProvider.revokeLoginAttempt(result);
    }

    @Override
    public LoginAttempt increaseAttempt(String username) {
        LoginAttempt result = loginAttemptProvider.findByUsername(username);
        if (result == null) {
            result = create(username);
        }

        result.increaseAttempt();
        loginAttemptProvider.saveLoginAttempt(result);
        return result;
    }

    @Override
    public boolean isAttemptExhausted(String username) {
        LoginAttempt result = loginAttemptProvider.findByUsername(username);
        if (result == null) {
            return false;
        }

        return result.getAttempts() >= maxAttempts;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(loginAttemptProvider);
    }

}

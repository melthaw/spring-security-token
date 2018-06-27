package in.clouthink.daas.security.token.spi.impl.mongodb;

import in.clouthink.daas.security.token.spi.LoginAttemptProvider;
import in.clouthink.daas.security.token.spi.impl.model.LoginAttemptEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The mongodb crud for login attempt.
 *
 * @since 1.7.0
 */
public class LoginAttemptProviderMongodbImpl implements LoginAttemptProvider<LoginAttemptEntity> {

    public static final Log logger = LogFactory.getLog(LoginAttemptProviderMongodbImpl.class);

    @Autowired
    private LoginAttemptEntityRepository loginAttemptEntityRepository;

    @Override
    public void saveLoginAttempt(LoginAttemptEntity loginAttempt) {
        logger.debug(String.format("Put loginAttempt:%s expiredAt:%s",
                                   loginAttempt.getUsername(),
                                   loginAttempt.getExpiredTime()));
        if (loginAttempt == null) {
            return;
        }
        loginAttemptEntityRepository.save(loginAttempt);
    }

    @Override
    public LoginAttemptEntity findByUsername(String username) {
        logger.debug(String.format("Get loginAttempt:%s", username));
        return loginAttemptEntityRepository.findFirstByUsername(username);
    }

    @Override
    public void revokeLoginAttempt(LoginAttemptEntity loginAttempt) {
        logger.debug(String.format("Del loginAttempt:%s", loginAttempt));
        if (loginAttempt == null) {
            return;
        }
        loginAttemptEntityRepository.delete(loginAttempt);
    }

}

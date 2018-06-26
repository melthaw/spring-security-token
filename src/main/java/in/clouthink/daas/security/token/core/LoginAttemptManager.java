package in.clouthink.daas.security.token.core;

/**
 * @author dz
 * @since 1.7.0
 */
public interface LoginAttemptManager extends LoginAttemptStrategySupport {

    LoginAttempt create(String username);

    LoginAttempt findByUsername(String username);

    LoginAttempt reset(String username);

    LoginAttempt increaseAttempt(String username);

}

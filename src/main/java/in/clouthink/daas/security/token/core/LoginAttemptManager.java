package in.clouthink.daas.security.token.core;

/**
 * @author dz
 * @since 1.7.0
 */
public interface LoginAttemptManager extends LoginAttemptStrategySupport {

    /**
     * Init login attempt to specified username
     *
     * @param username
     * @return
     */
    LoginAttempt create(String username);

    /**
     * Get login attempt by specified username
     *
     * @param username
     * @return
     */
    LoginAttempt findByUsername(String username);

    /**
     * Set the login attempt (actually the login attempt is revoked from store)
     *
     * @param username
     */
    void reset(String username);

    /**
     * Increase current attempt for specified username( if no login attempt found, will create one with zero value and increase to 1 )
     *
     * @param username
     * @return
     */
    LoginAttempt increaseAttempt(String username);

    /**
     * To judge the user exhaust all the attempt chance.
     *
     * @param username
     * @return true once the user attempt login to max
     */
    boolean isAttemptExhausted(String username);
}

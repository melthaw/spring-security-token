package in.clouthink.daas.security.token.core;

/**
 * @auther dz
 * @since 1.7.0
 */
public interface LoginAttemptStrategySupport {

    /**
     * @param timeout milli seconds
     */
    void setAttemptTimeout(long timeout);

}

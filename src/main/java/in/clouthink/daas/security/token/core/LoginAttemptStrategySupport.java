package in.clouthink.daas.security.token.core;

/**
 * @auther dz
 * @since 1.7.0
 */
public interface LoginAttemptStrategySupport {

    /**
     * @return
     */
    short getMaxAttempts();

    /**
     * @param maxAttempts The max attempts count, once equals or greater than it , the user will be locked.
     */
    void setMaxAttempts(short maxAttempts);

    /**
     * @return
     */
    long getAttemptTimeout();

    /**
     * @param timeout milli seconds , the user login attempts failure count duration ,once equals or greater than it , it will be reset or clear.
     */
    void setAttemptTimeout(long timeout);

}

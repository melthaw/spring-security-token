package in.clouthink.daas.security.token.core;

/**
 * TokenOptions
 */
public interface TokenOptions {

    /**
     * @param timeout milli seconds
     */
    void setTokenTimeout(long timeout);

    /**
     * @param refreshTokenInteval milli seconds
     */
    void setRefreshTokenInteval(long refreshTokenInteval);

    /**
     * @return true by default
     */
    boolean isMultiTokensAllowed();

    /**
     *
     */
    void enableMultiTokens();

    /**
     *
     */
    void disableMultiTokens();

}

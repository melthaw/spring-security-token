package in.clouthink.daas.security.token.core;

/**
 */
public interface TokenLifeSupport {
    
    /**
     * @param timeout
     *            milli seconds
     */
    public void setTokenTimeout(long timeout);
    
    /**
     * @param refreshTokenInteval
     *            milli seconds
     */
    public void setRefreshTokenInteval(long refreshTokenInteval);
    
}

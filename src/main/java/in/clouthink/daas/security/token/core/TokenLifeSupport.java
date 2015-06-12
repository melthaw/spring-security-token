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
    
    /**
     * @return true by default
     */
    public boolean isMultiTokensAllowed();
    
    /**
     * 
     */
    public void enableMultiTokens();
    
    /**
     *
     */
    public void disableMultiTokens();
    
}

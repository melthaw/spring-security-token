package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.spi.TokenProvider;
import in.clouthink.daas.security.token.spi.impl.model.TokenEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

public class DefaultTokenManager implements
                                TokenManager,
                                TokenLifeSupport,
                                InitializingBean {
    
    private TokenProvider tokenProvider;
    
    private long tokenTimeout = 60 * 60 * 1000;
    
    private long refreshTokenInteval = 3 * 60 * 1000;
    
    private boolean allowedMultiTokens = true;
    
    public DefaultTokenManager() {
    }
    
    public long getTokenTimeout() {
        return tokenTimeout;
    }
    
    public void setTokenTimeout(long tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }
    
    public long getRefreshTokenInteval() {
        return refreshTokenInteval;
    }
    
    public void setRefreshTokenInteval(long refreshTokenInteval) {
        this.refreshTokenInteval = refreshTokenInteval;
    }
    
    @Autowired
    public TokenProvider getTokenProvider() {
        return tokenProvider;
    }
    
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    @Override
    public void refreshToken(Token token) {
        if (null == token.getLatestTime()) {
            token.updateExpiredDate(tokenTimeout);
            tokenProvider.saveToken(token);
            return;
        }
        
        long lastTime = token.getLatestTime().getTime();
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - lastTime) > refreshTokenInteval) {
            token.updateExpiredDate(tokenTimeout);
            tokenProvider.saveToken(token);
        }
    }
    
    @Override
    public Token createToken(User owner) {
        if (!allowedMultiTokens) {
            List<Token> tokens = tokenProvider.findByUser(owner);
            if (tokens != null) {
                for (Token token : tokens) {
                    tokenProvider.revokeToken(token);
                }
            }
        }
        
        Token token = TokenEntity.create(owner, tokenTimeout);
        tokenProvider.saveToken(token);
        return token;
    }
    
    @Override
    public Token findToken(String token) {
        Token result = tokenProvider.findByToken(token);
        if (result == null) {
            return null;
        }
        // auto revoke the token if the token is expired
        if (result.getExpiredDate().getTime() < System.currentTimeMillis()) {
            tokenProvider.revokeToken(result);
            return null;
        }
        return result;
    }
    
    @Override
    public void revokeToken(String token) {
        tokenProvider.revokeToken(findToken(token));
    }
    
    @Override
    public boolean isMultiTokensAllowed() {
        return this.allowedMultiTokens;
    }
    
    @Override
    public void enableMultiTokens() {
        this.allowedMultiTokens = true;
    }
    
    @Override
    public void disableMultiTokens() {
        this.allowedMultiTokens = false;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tokenProvider);
    }
}

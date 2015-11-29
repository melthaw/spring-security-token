package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.spi.TokenProvider;
import in.clouthink.daas.security.token.spi.impl.model.TokenEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultTokenManager implements
                                 TokenManager,
                                 TokenLifeSupport,
                                 InitializingBean {
                                 
    private TokenProvider tokenProvider;
    
    private short maxAllowedTokenPerUser = 3;
    
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
                int existedTokenCount = tokens.size();
                if (existedTokenCount > (maxAllowedTokenPerUser - 1)) {
                    Collections.sort(tokens, new Comparator<Token>() {
                        @Override
                        public int compare(Token o1, Token o2) {
                            if (o1.getExpiredDate() != null
                                && o2.getExpiredDate() != null) {
                                long o1expiredTimestamp = o1.getExpiredDate()
                                                            .getTime();
                                long o2expiredTimestamp = o2.getExpiredDate()
                                                            .getTime();
                                if (o1expiredTimestamp == o2expiredTimestamp) {
                                    return 0;
                                }
                                return o1expiredTimestamp > o2expiredTimestamp ? -1
                                                                               : 1;
                            }
                            return 0;
                        }
                    });
                    
                    for (int i = 0; i < existedTokenCount; i++) {
                        Token token = tokens.get(i);
                        if (i < (maxAllowedTokenPerUser - 1)) {
                            token.disable();
                            tokenProvider.saveToken(token);
                        }
                        else {
                            tokenProvider.revokeToken(token);
                        }
                        
                    }
                }
                else {
                    for (Token token : tokens) {
                        token.disable();
                        tokenProvider.saveToken(token);
                    }
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

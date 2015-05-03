package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.spi.TokenProvider;
import in.clouthink.daas.security.token.spi.impl.model.TokenEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class DefaultTokenManager implements
                                TokenManager,
                                TokenLifeSupport,
                                InitializingBean {
    
    private TokenProvider tokenProvider;
    
    private long tokenTimeout = 60 * 60 * 1000;
    
    public DefaultTokenManager() {
    }
    
    public long getTokenTimeout() {
        return tokenTimeout;
    }
    
    public void setTokenTimeout(long tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }
    
    @Autowired
    public TokenProvider getTokenProvider() {
        return tokenProvider;
    }
    
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    @Override
    public void refreshToken(String token) {
        TokenEntity result = (TokenEntity) tokenProvider.findByToken(token);
        if (result != null) {
            result.updateExpiredDate(tokenTimeout);
            tokenProvider.saveToken(result);
        }
    }
    
    @Override
    public Token createToken(User owner) {
        Token token = TokenEntity.create(owner, tokenTimeout);
        tokenProvider.saveToken(token);
        return token;
    }
    
    @Override
    public Token findToken(String token) {
        Token result = tokenProvider.findByToken(token);
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
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tokenProvider);
    }
}

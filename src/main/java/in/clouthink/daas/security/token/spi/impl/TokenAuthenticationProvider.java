package in.clouthink.daas.security.token.spi.impl;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import in.clouthink.daas.security.token.core.*;
import in.clouthink.daas.security.token.exception.*;
import in.clouthink.daas.security.token.spi.AuthenticationProvider;
import in.clouthink.daas.security.token.spi.IdentityProvider;

/**
 */
public class TokenAuthenticationProvider implements
                                        AuthenticationProvider<TokenAuthenticationRequest>,
                                        InitializingBean {
    
    private IdentityProvider identityProvider;
    
    private TokenManager tokenManager;
    
    public void setIdentityProvider(IdentityProvider identityProvider) {
        this.identityProvider = identityProvider;
    }
    
    public TokenManager getTokenManager() {
        return tokenManager;
    }
    
    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
    
    public IdentityProvider getIdentityProvider() {
        return identityProvider;
    }
    
    @Override
    public boolean supports(AuthenticationRequest request) {
        return (request instanceof TokenAuthenticationRequest);
    }
    
    @Override
    public Authentication authenticate(TokenAuthenticationRequest tokenAuthenticationRequest) {
        String principal = tokenAuthenticationRequest.getPrincipal();
        
        Token token = tokenManager.findToken(principal);
        
        if (token == null) {
            throw new InvalidTokenException();
        }
        
        Date expiredDate = token.getExpiredDate();
        
        if (expiredDate.getTime() < System.currentTimeMillis()) {
            throw new TokenExpiredException();
        }
        
        User user = token.getOwner();
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.isExpired()) {
            throw new UserExpiredException();
        }
        if (user.isLocked()) {
            throw new UserLockedException();
        }
        if (!user.isEnabled()) {
            throw new UserDisabledException();
        }
        
        return new DefaultAuthentication(token);
    }
    
    @Override
    public void revoke(Authentication authentication) {
        Token token = authentication.currentToken();
        tokenManager.revokeToken(token.getToken());
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(identityProvider);
        Assert.notNull(tokenManager);
    }
    
}

package in.clouthink.daas.security.token.spi.impl;

import in.clouthink.daas.security.token.core.*;
import in.clouthink.daas.security.token.federation.FederationRequest;
import in.clouthink.daas.security.token.spi.FederationProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 *
 */
public class SimpleFederationProvider implements
                                      FederationProvider<SimpleFederationRequest>,
                                      InitializingBean {
                                      
    private TokenManager tokenManager;
    
    public TokenManager getTokenManager() {
        return tokenManager;
    }
    
    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
    
    @Override
    public boolean supports(FederationRequest request) {
        return (request instanceof SimpleFederationRequest);
    }
    
    @Override
    public Authentication login(SimpleFederationRequest request) {
        Token token = tokenManager.createToken(request.getPrincipal());
        return new DefaultAuthentication(token);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tokenManager);
    }
    
}

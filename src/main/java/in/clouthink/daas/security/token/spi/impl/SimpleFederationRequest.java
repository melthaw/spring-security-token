package in.clouthink.daas.security.token.spi.impl;

import in.clouthink.daas.security.token.core.User;
import in.clouthink.daas.security.token.federation.FederationRequest;

/**
 *
 */
public class SimpleFederationRequest implements FederationRequest<User> {
    
    private User principal;
    
    public SimpleFederationRequest(User principal) {
        this.principal = principal;
    }
    
    @Override
    public User getPrincipal() {
        return principal;
    }
    
}

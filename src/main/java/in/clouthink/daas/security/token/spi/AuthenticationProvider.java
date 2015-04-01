package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationRequest;

/**
 */
public interface AuthenticationProvider<T extends AuthenticationRequest> {
    
    public boolean supports(AuthenticationRequest request);
    
    public Authentication authenticate(T t);
    
    public void revoke(Authentication authentication);
    
}

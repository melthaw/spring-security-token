package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationRequest;

/**
 */
public interface AuthenticationProvider<T extends AuthenticationRequest> {
    
    boolean supports(AuthenticationRequest request);
    
    Authentication authenticate(T t);
    
    void revoke(Authentication authentication);
    
}

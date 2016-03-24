package in.clouthink.daas.security.token.federation;

import in.clouthink.daas.security.token.core.Authentication;

/**
 *
 */
public interface FederationService {
    
    Authentication login(FederationRequest request);
    
    void logout(Authentication authentication);
    
}

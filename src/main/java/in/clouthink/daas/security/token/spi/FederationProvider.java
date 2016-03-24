package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.federation.FederationRequest;

/**
 *
 */
public interface FederationProvider<T extends FederationRequest> {
    
    boolean supports(FederationRequest request);
    
    Authentication login(T request);
    
}

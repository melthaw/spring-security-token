package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.acl.AccessRequest;
import in.clouthink.daas.security.token.core.acl.AccessResponse;

/**
 */
public interface AuthorizationProvider<T extends AccessRequest> {
    
    public AccessResponse authorize(T accessRequest);
    
}

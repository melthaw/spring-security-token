package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.core.acl.AccessRequest;

public interface AuthorizationManager {
    
    public void authorize(AccessRequest accessRequest);
    
}

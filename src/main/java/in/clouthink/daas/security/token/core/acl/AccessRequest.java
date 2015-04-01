package in.clouthink.daas.security.token.core.acl;

import in.clouthink.daas.security.token.core.Authentication;

/**
 */
public interface AccessRequest {
    
    public Authentication getAuthentication();
    
    public String getTarget();
    
    public String getOperation();
    
}

package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.core.acl.AccessRequest;
import in.clouthink.daas.security.token.core.acl.AccessResponse;
import in.clouthink.daas.security.token.exception.AccessDeniedException;
import in.clouthink.daas.security.token.exception.AuthenticationFailureException;
import in.clouthink.daas.security.token.exception.AuthorizationFailureException;
import in.clouthink.daas.security.token.spi.AuthorizationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class DefaultAuthorizationManager implements AuthorizationManager {

    private AuthorizationStrategy authorizationStrategy = AuthorizationStrategy.DENY_IF_ABANDON;
    
    private List<AuthorizationProvider> providers = new ArrayList<AuthorizationProvider>();
    
    public AuthorizationStrategy getAuthorizationStrategy() {
        return authorizationStrategy;
    }
    
    public void setAuthorizationStrategy(AuthorizationStrategy authorizationStrategy) {
        this.authorizationStrategy = authorizationStrategy;
    }
    
    public void addAuthorizationProvider(AuthorizationProvider provider) {
        providers.add(provider);
    }
    
    public List<AuthorizationProvider> getProviders() {
        return providers;
    }
    
    @Autowired
    public void setProviders(List<AuthorizationProvider> providers) {
        this.providers = providers;
    }
    
    @Override
    public void authorize(AccessRequest accessRequest) {
        if (accessRequest == null) {
            return;
        }
        
        for (AuthorizationProvider provider : providers) {
            AccessResponse accessResponse = provider.authorize(accessRequest);
            if (AccessResponse.APPROVED == accessResponse) {
                return;
            }
            if (AccessResponse.REFUESD == accessResponse) {
                throw new AccessDeniedException();
            }
        }
        
        if (AuthorizationStrategy.DENY_IF_ABANDON == authorizationStrategy) {
            throw new AuthorizationFailureException();
        }
    }
    
}

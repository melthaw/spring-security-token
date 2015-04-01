package in.clouthink.daas.security.token.support;

import in.clouthink.daas.security.token.SecurityContext;
import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.SecurityContextManager;

public class SecurityUtils {
    
    public static Authentication currentAuthentication() {
        SecurityContext context = SecurityContextManager.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }
    
}

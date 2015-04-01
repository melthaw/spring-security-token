package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.SecurityContext;
import org.springframework.util.Assert;

/**
 */
public class SecurityContextManager {
    
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<SecurityContext>();
    
    public static void clearContext() {
        contextHolder.remove();
    }
    
    public static SecurityContext getContext() {
        SecurityContext ctx = contextHolder.get();
        
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        
        return ctx;
    }
    
    public static void setContext(SecurityContext context) {
        Assert.notNull(context,
                       "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }
    
    public static SecurityContext createEmptyContext() {
        return new DefaultSecurityContext();
    }
    
}

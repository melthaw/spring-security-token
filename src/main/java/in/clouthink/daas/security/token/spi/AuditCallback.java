package in.clouthink.daas.security.token.spi;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 1.2.2
 */
public interface AuditCallback {
    
    /**
     * @param request
     * @param isAuthenticated
     */
    public void auditLogin(HttpServletRequest request, boolean isAuthenticated);
    
    /**
     * @param request
     */
    public void auditLogout(HttpServletRequest request);
    
}

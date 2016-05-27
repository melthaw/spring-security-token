package in.clouthink.daas.security.token.spi;

import javax.servlet.http.HttpServletRequest;

/**
 * Please replace with audit event listener
 *
 * @since 1.2.2
 */
@Deprecated
public interface AuditCallback {

	/**
	 * @param request
	 * @param isAuthenticated
	 */
	void auditLogin(HttpServletRequest request, boolean isAuthenticated);

	/**
	 * @param request
	 */
	void auditLogout(HttpServletRequest request);

}

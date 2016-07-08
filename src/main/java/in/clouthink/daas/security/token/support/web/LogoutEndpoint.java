package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationManager;
import in.clouthink.daas.security.token.core.TokenAuthenticationRequest;
import in.clouthink.daas.security.token.event.authentication.HttpLogoutEvent;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import in.clouthink.daas.security.token.spi.AuditCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutEndpoint extends GenericFilterBean implements ApplicationContextAware {

	private static final Log logger = LogFactory.getLog(LogoutEndpoint.class);

	private static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";

	//@since 1.5.0
	private ApplicationContext applicationContext;

	private AuthorizationFailureHandler authorizationFailureHandler = new DefaultAuthorizationFailureHandler();

	private RequestMatcher logoutRequestMatcher;

	private AuthenticationManager authenticationManager;

	private TokenResolver tokenResolver = new BearerAuthorizationHeaderTokenResolver();

	private AuditCallback auditCallback;

	private boolean useStrict = true;

	/**
	 *
	 */
	public LogoutEndpoint() {
		this.logoutRequestMatcher = new AntPathRequestMatcher("/token/logout");
	}

	/**
	 * @param logoutFilterProcessesUrl
	 */
	public LogoutEndpoint(String logoutFilterProcessesUrl) {
		this.logoutRequestMatcher = new AntPathRequestMatcher(logoutFilterProcessesUrl);
	}

	/**
	 * @param logoutRequestMatcher
	 */
	public LogoutEndpoint(RequestMatcher logoutRequestMatcher) {
		Assert.notNull(logoutRequestMatcher, "logoutRequestMatcher cannot be null");
		this.logoutRequestMatcher = logoutRequestMatcher;

	}

	/**
	 * @param applicationContext
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setLogoutProcessesUrl(String logoutFilterProcessesUrl) {
		this.logoutRequestMatcher = new AntPathRequestMatcher(logoutFilterProcessesUrl);
	}

	public final void setLogoutRequestMatcher(RequestMatcher logoutRequestMatcher) {
		Assert.notNull(logoutRequestMatcher, "logoutRequestMatcher cannot be null");
		this.logoutRequestMatcher = logoutRequestMatcher;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setAuthorizationFailureHandler(AuthorizationFailureHandler authorizationFailureHandler) {
		this.authorizationFailureHandler = authorizationFailureHandler;
	}

	public TokenResolver getTokenResolver() {
		return tokenResolver;
	}

	public void setTokenResolver(TokenResolver tokenResolver) {
		this.tokenResolver = tokenResolver;
	}

	public boolean isUseStrict() {
		return useStrict;
	}

	public void setUseStrict(boolean useStrict) {
		this.useStrict = useStrict;
	}

	@Override
	public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		logger.trace("doFilter start");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (isLogoutMatched(request, response)) {
			logger.trace("doLogout matched");
			doLogout(request, response, chain);
		}
		else {
			logger.trace("doLogout un-matched, skip it");
			chain.doFilter(request, response);
		}
	}

	private void doLogout(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.trace("doLogout start");
		try {
			String tokenValue = tokenResolver.resolve(request, response);
			Authentication authentication = authenticationManager.login(new TokenAuthenticationRequest(tokenValue));
			authenticationManager.logout(authentication);

			if (auditCallback != null) {
				try {
					auditCallback.auditLogout(request);
				}
				catch (Throwable e1) {
				}
			}

			applicationContext.publishEvent(new HttpLogoutEvent(authentication, request));
		}
		catch (Exception e) {
			logger.error(e, e);

			if (useStrict) {
				authorizationFailureHandler.handle(request, response, e);
			}
		}
	}

	protected boolean isLogoutMatched(HttpServletRequest request, HttpServletResponse response) {
		return logoutRequestMatcher.matches(request);
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationManager, "authenticationManager must be specified");
	}
}

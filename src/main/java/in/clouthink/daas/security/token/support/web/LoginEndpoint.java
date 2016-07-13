package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationManager;
import in.clouthink.daas.security.token.core.UsernamePasswordAuthenticationRequest;
import in.clouthink.daas.security.token.event.authentication.HttpLoginEvent;
import in.clouthink.daas.security.token.exception.AuthenticationException;
import in.clouthink.daas.security.token.exception.AuthenticationFailureException;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import in.clouthink.daas.security.token.spi.AuditCallback;
import in.clouthink.daas.security.token.spi.PreLoginHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginEndpoint extends GenericFilterBean implements ApplicationContextAware, Ordered {

	private static final Log logger = LogFactory.getLog(LoginEndpoint.class);

	public static final String SECURITY_FORM_USERNAME_KEY = "username";

	public static final String SECURITY_FORM_PASSWORD_KEY = "password";

	//@since 2.0.0
	private int order = Ordered.HIGHEST_PRECEDENCE + 1;

	private String usernameParameter = SECURITY_FORM_USERNAME_KEY;

	private String passwordParameter = SECURITY_FORM_PASSWORD_KEY;

	private boolean postOnly = true;

	//@since 1.5.0
	private ApplicationContext applicationContext;

	private AuthenticationSuccessHandler authenticationSuccessHandler = new DefaultAuthenticationSuccessHandler();

	private AuthenticationFailureHandler authenticationFailureHandler = new DefaultAuthenticationFailureHandler();

	private RequestMatcher loginRequestMatcher;

	private AuthenticationManager authenticationManager;

	private AuditCallback auditCallback;

	private PreLoginHandler preLoginHandler;

	/**
	 *
	 */
	public LoginEndpoint() {
		this.loginRequestMatcher = new AntPathRequestMatcher("/token/login");
	}

	/**
	 * @param loginFilterProcessesUrl
	 */
	public LoginEndpoint(String loginFilterProcessesUrl) {
		this.loginRequestMatcher = new AntPathRequestMatcher(loginFilterProcessesUrl);
	}

	/**
	 * @param loginRequestMatcher
	 */
	public LoginEndpoint(RequestMatcher loginRequestMatcher) {
		Assert.notNull(loginRequestMatcher, "loginRequestMatcher cannot be null");
		this.loginRequestMatcher = loginRequestMatcher;

	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the order
	 */
	@Override
	public int getOrder() {
		return this.order;
	}

	/**
	 * @param applicationContext
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @param loginFilterProcessesUrl
	 */
	public void setLoginProcessesUrl(String loginFilterProcessesUrl) {
		this.loginRequestMatcher = new AntPathRequestMatcher(loginFilterProcessesUrl);
	}

	/**
	 * @param loginRequestMatcher
	 */
	public final void setLoginRequestMatcher(RequestMatcher loginRequestMatcher) {
		Assert.notNull(loginRequestMatcher, "loginRequestMatcher cannot be null");
		this.loginRequestMatcher = loginRequestMatcher;
	}

	public String getUsernameParameter() {
		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}

	public boolean isPostOnly() {
		return postOnly;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return authenticationSuccessHandler;
	}

	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public AuditCallback getAuditCallback() {
		return auditCallback;
	}

	public void setAuditCallback(AuditCallback auditCallback) {
		Assert.notNull(auditCallback, "auditCallback cannot be null");
		this.auditCallback = auditCallback;
	}

	public PreLoginHandler getPreLoginHandler() {
		return preLoginHandler;
	}

	public void setPreLoginHandler(PreLoginHandler preLoginHandler) {
		this.preLoginHandler = preLoginHandler;
	}

	@Override
	public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		logger.trace("doFilter start");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (isLoginMatched(request, response)) {
			logger.trace("doLogin matched");
			doLogin(request, response, chain);
		}
		else {
			logger.trace("doLogin un-matched, skip it");
			chain.doFilter(request, response);
		}
	}

	private void doLogin(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.trace("doLogin start");
		try {
			if (postOnly && !"POST".equals(request.getMethod())) {
				throw new AuthenticationException("Authentication method not supported: " + request.getMethod());
			}

			if (preLoginHandler != null) {
				preLoginHandler.handle(request, response);
			}

			String username = obtainUsername(request);
			String password = obtainPassword(request);

			if (username == null) {
				throw new AuthenticationFailureException(String.format("The %s required.", usernameParameter));
			}

			if (password == null) {
				throw new AuthenticationFailureException(String.format("The %s required.", passwordParameter));
			}

			username = username.trim();

			UsernamePasswordAuthenticationRequest authRequest = new UsernamePasswordAuthenticationRequest(username,
																										  password);

			Authentication authentication = authenticationManager.login(authRequest);

			//deprecated after 1.5.0
			if (auditCallback != null) {
				try {
					auditCallback.auditLogin(request, true);
				}
				catch (Throwable e) {
				}
			}

			//@since 1.5.0
			applicationContext.publishEvent(new HttpLoginEvent(request, authentication));

			authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		}
		catch (Exception e) {
			logger.error(e, e);

			//deprecated after 1.5.0
			if (auditCallback != null) {
				try {
					auditCallback.auditLogin(request, false);
				}
				catch (Throwable e1) {
				}
			}

			//@since 1.5.0
			applicationContext.publishEvent(new HttpLoginEvent(request, e));

			authenticationFailureHandler.handle(request, response, e);
		}
	}

	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	protected boolean isLoginMatched(HttpServletRequest request, HttpServletResponse response) {
		if (postOnly) {
			return loginRequestMatcher.matches(request) && "POST".equals(request.getMethod());
		}
		return loginRequestMatcher.matches(request);
	}

	@Override
	public void afterPropertiesSet() {
		logger.trace("afterPropertiesSet");
		Assert.notNull(authenticationManager, "authenticationManager must be specified");
	}
}

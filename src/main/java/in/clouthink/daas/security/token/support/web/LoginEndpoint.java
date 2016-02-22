package in.clouthink.daas.security.token.support.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.clouthink.daas.security.token.exception.AuthenticationFailureException;
import in.clouthink.daas.security.token.exception.ErrorConstants;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import in.clouthink.daas.security.token.spi.AuditCallback;
import in.clouthink.daas.security.token.support.i18n.DefaultMessageProvider;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationManager;
import in.clouthink.daas.security.token.core.UsernamePasswordAuthenticationRequest;
import in.clouthink.daas.security.token.exception.AuthenticationException;

public class LoginEndpoint extends GenericFilterBean {

    private static final Log logger = LogFactory.getLog(LoginEndpoint.class);

    public static final String SECURITY_FORM_USERNAME_KEY = "username";

    public static final String SECURITY_FORM_PASSWORD_KEY = "password";

    private String usernameParameter = SECURITY_FORM_USERNAME_KEY;

    private String passwordParameter = SECURITY_FORM_PASSWORD_KEY;

    private boolean postOnly = true;

    private AuthenticationSuccessHandler authenticationSuccessHandler = new DefaultAuthenticationSuccessHandler();

    private AuthenticationFailureHandler authenticationFailureHandler = new DefaultAuthenticationFailureHandler();

    private RequestMatcher loginRequestMatcher;

    private AuthenticationManager authenticationManager;

    private AuditCallback auditCallback;

    private MessageProvider messageProvider = new DefaultMessageProvider();

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
        Assert.notNull(loginRequestMatcher,
                       "loginRequestMatcher cannot be null");
        this.loginRequestMatcher = loginRequestMatcher;

    }

    public void setLoginProcessesUrl(String loginFilterProcessesUrl) {
        this.loginRequestMatcher = new AntPathRequestMatcher(loginFilterProcessesUrl);
    }

    public final void setLoginRequestMatcher(RequestMatcher loginRequestMatcher) {
        Assert.notNull(loginRequestMatcher,
                       "loginRequestMatcher cannot be null");
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

    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public final void doFilter(ServletRequest req,
                               ServletResponse res,
                               FilterChain chain) throws ServletException,
                                                  IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (isLoginMatched(request, response)) {
            doLogin(request, response, chain);
        }
        else {
            chain.doFilter(request, response);
        }
    }

    private void doLogin(HttpServletRequest request,
                         HttpServletResponse response,
                         FilterChain chain) throws IOException,
                                            ServletException {
        try {
            if (postOnly && !"POST".equals(request.getMethod())) {
                throw new AuthenticationException("Authentication method not supported: "
                                                  + request.getMethod());
            }

            String username = obtainUsername(request);
            String password = obtainPassword(request);

            if (username == null || StringUtils.isEmpty(username)) {
                throw new AuthenticationFailureException(messageProvider.getMessage(ErrorConstants.USERNAME_REQUIRED));
            }

            if (password == null || StringUtils.isEmpty(password)) {
                throw new AuthenticationFailureException(messageProvider.getMessage(ErrorConstants.PASSWORD_REQUIRED));
            }

            username = username.trim();

            UsernamePasswordAuthenticationRequest authRequest = new UsernamePasswordAuthenticationRequest(username,
                                                                                                          password);

            Authentication authentication = authenticationManager.login(authRequest);

            authenticationSuccessHandler.onAuthenticationSuccess(request,
                                                                 response,
                                                                 authentication);

            if (auditCallback != null) {
                try {
                    auditCallback.auditLogin(request, true);
                }
                catch (Throwable e) {
                }
            }
        }
        catch (Exception e) {
            logger.error(e, e);
            if (auditCallback != null) {
                try {
                    auditCallback.auditLogin(request, false);
                }
                catch (Throwable e1) {
                }
            }
            authenticationFailureHandler.handle(request, response, e);
        }
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    protected boolean isLoginMatched(HttpServletRequest request,
                                     HttpServletResponse response) {
        return loginRequestMatcher.matches(request);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager,
                       "authenticationManager must be specified");
    }
}

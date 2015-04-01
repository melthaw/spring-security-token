package in.clouthink.daas.security.token.support.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationManager;
import in.clouthink.daas.security.token.core.TokenAuthenticationRequest;
import in.clouthink.daas.security.token.exception.AuthenticationRequiredException;
import org.springframework.web.filter.OncePerRequestFilter;

public class LogoutEndpoint extends GenericFilterBean {
    
    private static final Log logger = LogFactory.getLog(LogoutEndpoint.class);
    
    private static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    
    private AuthorizationFailureHandler authorizationFailureHandler = new DefaultAuthorizationFailureHandler();
    
    private RequestMatcher logoutRequestMatcher;
    
    private AuthenticationManager authenticationManager;
    
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
        Assert.notNull(logoutRequestMatcher,
                       "logoutRequestMatcher cannot be null");
        this.logoutRequestMatcher = logoutRequestMatcher;
        
    }
    
    public void setLogoutProcessesUrl(String logoutFilterProcessesUrl) {
        this.logoutRequestMatcher = new AntPathRequestMatcher(logoutFilterProcessesUrl);
    }
    
    public final void setLogoutRequestMatcher(RequestMatcher logoutRequestMatcher) {
        Assert.notNull(logoutRequestMatcher,
                       "logoutRequestMatcher cannot be null");
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
    
    @Override
    public final void doFilter(ServletRequest req,
                               ServletResponse res,
                               FilterChain chain) throws ServletException,
                                                 IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (isLogoutMatched(request, response)) {
            doLogout(request, response, chain);
        }
        else {
            chain.doFilter(request, response);
        }
    }
    
    private void doLogout(HttpServletRequest request,
                          HttpServletResponse response,
                          FilterChain chain) throws IOException,
                                            ServletException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.isEmpty(authHeader)) {
                throw new AuthenticationRequiredException("Authorization header required");
            }
            
            if (authHeader.length() <= HEADER_AUTHORIZATION_PREFIX.length()) {
                throw new IllegalArgumentException("Unrecognized Authorization header.");
            }
            
            String base64Final = authHeader.substring(HEADER_AUTHORIZATION_PREFIX.length());
            String tokenValue = null;
            try {
                tokenValue = new String(Base64.decode(base64Final.getBytes("UTF-8")));
            }
            catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Unrecognized Authorization header.");
            }
            
            Authentication authentication = authenticationManager.login(new TokenAuthenticationRequest(tokenValue));
            authenticationManager.logout(authentication);
        }
        catch (Exception e) {
            logger.error(e, e);
            authorizationFailureHandler.handle(request, response, e);
        }
    }
    
    protected boolean isLogoutMatched(HttpServletRequest request,
                                      HttpServletResponse response) {
        return logoutRequestMatcher.matches(request);
    }
    
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager,
                       "authenticationManager must be specified");
    }
}

package in.clouthink.daas.security.token.support.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.clouthink.daas.security.token.spi.AuditCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationManager;
import in.clouthink.daas.security.token.core.SecurityContextManager;
import in.clouthink.daas.security.token.core.TokenAuthenticationRequest;
import in.clouthink.daas.security.token.exception.AuthenticationRequiredException;
import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;

public class AuthenticationFilter extends GenericFilterBean {
    
    private static final Log logger = LogFactory.getLog(AuthenticationFilter.class);
    
    private static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    
    private AuthorizationFailureHandler authorizationFailureHandler = new DefaultAuthorizationFailureHandler();
    
    private RequestMatcher urlRequestMatcher;
    
    private RequestMatcher ignoredUrlRequestMatcher;
    
    private AuthenticationManager authenticationManager;

    /**
     *
     */
    public AuthenticationFilter() {
        this.urlRequestMatcher = new AntPathRequestMatcher("/api**");
    }
    
    /**
     *
     */
    public AuthenticationFilter(String filterProcessesUrl) {
        this.urlRequestMatcher = new AntPathRequestMatcher(filterProcessesUrl);
    }
    
    /**
     *
     */
    public AuthenticationFilter(RequestMatcher urlRequestMatcher) {
        this.urlRequestMatcher = urlRequestMatcher;
    }
    
    public void setProcessesUrl(String filterProcessesUrl) {
        this.urlRequestMatcher = new AntPathRequestMatcher(filterProcessesUrl);
    }
    
    public final void setUrlRequestMatcher(RequestMatcher urlRequestMatcher) {
        Assert.notNull(urlRequestMatcher, "urlRequestMatcher cannot be null");
        this.urlRequestMatcher = urlRequestMatcher;
    }

    public void setIgnoredProcessesUrl(String ignoreFilterProcessesUrl) {
        this.ignoredUrlRequestMatcher = new AntPathRequestMatcher(ignoreFilterProcessesUrl);
    }
    
    public void setIgnoredUrlRequestMatcher(RequestMatcher ignoredUrlRequestMatcher) {
        Assert.notNull(ignoredUrlRequestMatcher,
                       "ignoredUrlRequestMatcher cannot be null");
        this.ignoredUrlRequestMatcher = ignoredUrlRequestMatcher;
    }
    
    public AuthorizationFailureHandler getAuthorizationFailureHandler() {
        return authorizationFailureHandler;
    }
    
    public void setAuthorizationFailureHandler(AuthorizationFailureHandler authorizationFailureHandler) {
        this.authorizationFailureHandler = authorizationFailureHandler;
    }
    
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
    
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public final void doFilter(ServletRequest req,
                               ServletResponse res,
                               FilterChain chain) throws ServletException,
                                                  IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (isUrlProcessingMatched(request, response)) {
            doAuthentication(request, response, chain);
        }
        else {
            chain.doFilter(request, response);
        }
    }
    
    private void doAuthentication(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException,
                                                     ServletException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.isEmpty(authHeader)) {
                throw new AuthenticationRequiredException();
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
            SecurityContextManager.getContext()
                                  .setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        catch (Exception e) {
            logger.error(e, e);
            authorizationFailureHandler.handle(request, response, e);
        }
        finally {
            SecurityContextManager.clearContext();
        }
    }
    
    protected boolean isUrlProcessingMatched(HttpServletRequest request,
                                             HttpServletResponse response) {
        if (ignoredUrlRequestMatcher != null) {
            if (ignoredUrlRequestMatcher.matches(request)) {
                return false;
            }
        }
        return urlRequestMatcher.matches(request);
    }
    
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager,
                       "authenticationManager must be specified");
    }
    
}

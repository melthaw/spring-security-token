package in.clouthink.daas.security.token.support.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationManager;
import in.clouthink.daas.security.token.core.SecurityContextManager;
import in.clouthink.daas.security.token.core.TokenAuthenticationRequest;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;

public class PreAuthenticationFilter extends GenericFilterBean {
    
    private static final Log logger = LogFactory.getLog(PreAuthenticationFilter.class);
    
    private TokenResolver tokenResolver = new BearerAuthorizationHeaderTokenResolver();
    
    private AuthorizationFailureHandler authorizationFailureHandler = new DefaultAuthorizationFailureHandler();
    
    private boolean corsEnabled = false;
    
    private boolean strictTokenEnabled = true;
    
    private RequestMatcher urlRequestMatcher;
    
    private AuthenticationManager authenticationManager;
    
    /**
     *
     */
    public PreAuthenticationFilter() {
        this.urlRequestMatcher = new AntPathRequestMatcher("/api**");
    }
    
    /**
     *
     */
    public PreAuthenticationFilter(String filterProcessesUrl) {
        this.urlRequestMatcher = new AntPathRequestMatcher(filterProcessesUrl);
    }
    
    /**
     *
     */
    public PreAuthenticationFilter(RequestMatcher urlRequestMatcher) {
        this.urlRequestMatcher = urlRequestMatcher;
    }
    
    public void setProcessesUrl(String filterProcessesUrl) {
        this.urlRequestMatcher = new AntPathRequestMatcher(filterProcessesUrl);
    }
    
    public final void setUrlRequestMatcher(RequestMatcher urlRequestMatcher) {
        Assert.notNull(urlRequestMatcher, "urlRequestMatcher cannot be null");
        this.urlRequestMatcher = urlRequestMatcher;
    }
    
    public TokenResolver getTokenResolver() {
        return tokenResolver;
    }
    
    public void setTokenResolver(TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }
    
    public AuthorizationFailureHandler getAuthorizationFailureHandler() {
        return authorizationFailureHandler;
    }
    
    public void setAuthorizationFailureHandler(AuthorizationFailureHandler authorizationFailureHandler) {
        this.authorizationFailureHandler = authorizationFailureHandler;
    }
    
    public boolean isCorsEnabled() {
        return corsEnabled;
    }
    
    public void setCorsEnabled(boolean corsEnabled) {
        this.corsEnabled = corsEnabled;
    }
    
    public boolean isStrictTokenEnabled() {
        return strictTokenEnabled;
    }
    
    public void setStrictTokenEnabled(boolean strictTokenEnabled) {
        this.strictTokenEnabled = strictTokenEnabled;
    }
    
    public RequestMatcher getUrlRequestMatcher() {
        return urlRequestMatcher;
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
        if (isUrlProcessingMatched(request, response)
            && !isPreflightRequest(request)) {
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
            String tokenValue = null;
            try {
                tokenValue = tokenResolver.resolve(request, response);
            }
            catch (Exception e) {
                if (strictTokenEnabled) {
                    logger.error(e, e);
                    authorizationFailureHandler.handle(request, response, e);
                    return;
                }
            }
            
            if (tokenValue == null) {
                chain.doFilter(request, response);
                return;
            }
            
            try {
                Authentication authentication = authenticationManager.login(new TokenAuthenticationRequest(tokenValue));
                SecurityContextManager.getContext()
                                      .setAuthentication(authentication);
            }
            catch (Exception e) {
                logger.error(e, e);
                authorizationFailureHandler.handle(request, response, e);
                return;
            }
            chain.doFilter(request, response);
        }
        finally {
            SecurityContextManager.clearContext();
        }
    }
    
    private boolean isPreflightRequest(HttpServletRequest request) {
        return corsEnabled && "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
    
    protected boolean isUrlProcessingMatched(HttpServletRequest request,
                                             HttpServletResponse response) {
        return urlRequestMatcher.matches(request);
    }
    
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager,
                       "authenticationManager must be specified");
    }
    
}

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthorizationManager;
import in.clouthink.daas.security.token.core.acl.UrlAccessRequest;
import in.clouthink.daas.security.token.exception.AuthenticationRequiredException;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import in.clouthink.daas.security.token.support.SecurityUtils;

public class AuthorizationFilter extends GenericFilterBean {
    
    private static final Log logger = LogFactory.getLog(AuthorizationFilter.class);
    
    private AuthorizationFailureHandler authorizationFailureHandler = new DefaultAuthorizationFailureHandler();
    
    private RequestMatcher urlRequestMatcher;
    
    private RequestMatcher ignoredUrlRequestMatcher;
    
    private AuthorizationManager authorizationManager;
    
    private boolean enableCors = false;
    
    /**
     *
     */
    public AuthorizationFilter() {
        this.urlRequestMatcher = new AntPathRequestMatcher("/api**");
    }
    
    /**
     *
     */
    public AuthorizationFilter(String filterProcessesUrl) {
        this.urlRequestMatcher = new AntPathRequestMatcher(filterProcessesUrl);
    }
    
    /**
     *
     */
    public AuthorizationFilter(RequestMatcher urlRequestMatcher) {
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
    
    public AuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }
    
    @Autowired
    public void setAuthorizationManager(AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }
    
    public void setAuthorizationFailureHandler(AuthorizationFailureHandler authorizationFailureHandler) {
        this.authorizationFailureHandler = authorizationFailureHandler;
    }
    
    public void setEnableCors(boolean enableCors) {
        this.enableCors = enableCors;
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
            doAuthorization(request, response, chain);
        }
        else {
            chain.doFilter(request, response);
        }
    }
    
    private void doAuthorization(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain chain) throws IOException,
                                                    ServletException {
        try {
            Authentication authentication = SecurityUtils.currentAuthentication();
            if (authentication == null) {
                throw new AuthenticationRequiredException();
            }
            
            String httpMethod = request.getMethod();
            String url = request.getServletPath();
            String pathInfo = request.getPathInfo();
            String query = request.getQueryString();
            
            if (pathInfo != null || query != null) {
                StringBuilder sb = new StringBuilder(url);
                
                if (pathInfo != null) {
                    sb.append(pathInfo);
                }
                
                if (query != null) {
                    sb.append('?').append(query);
                }
                url = sb.toString();
            }
            
            authorizationManager.authorize(new UrlAccessRequest(authentication,
                                                                url,
                                                                httpMethod));
        }
        catch (Exception e) {
            logger.error(e, e);
            authorizationFailureHandler.handle(request, response, e);
            return;
        }
        chain.doFilter(request, response);
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
    
    private boolean isPreflightRequest(HttpServletRequest request) {
        return enableCors && "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
    
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authorizationManager,
                       "authorizationManager must be specified");
    }
    
}

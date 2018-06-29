package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.AuthenticationFeature;
import in.clouthink.daas.security.token.core.AuthorizationManager;
import in.clouthink.daas.security.token.core.FeatureConfigurer;
import in.clouthink.daas.security.token.core.acl.UrlAccessRequest;
import in.clouthink.daas.security.token.exception.AuthenticationRequiredException;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import in.clouthink.daas.security.token.support.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

public class AuthorizationFilter extends GenericFilterBean implements Ordered {

    private static final Log logger = LogFactory.getLog(AuthorizationFilter.class);

    //@since 1.2.0
    private int order = Ordered.HIGHEST_PRECEDENCE + 6;

    private AuthorizationFailureHandler authorizationFailureHandler = new DefaultAuthorizationFailureHandler();

    private RequestMatcher urlRequestMatcher;

    private RequestMatcher ignoredUrlRequestMatcher;

    private AuthorizationManager authorizationManager;

    //@since 1.6.0
    private FeatureConfigurer featureConfigurer;

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

    public void setFeatureConfigurer(FeatureConfigurer featureConfigurer) {
        this.featureConfigurer = featureConfigurer;
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
        Assert.notNull(ignoredUrlRequestMatcher, "ignoredUrlRequestMatcher cannot be null");
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


    @Override
    public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        logger.trace("doFilter start");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (isUrlProcessingMatched(request, response) && !isPreFlightRequest(request)) {
            logger.trace("doAuthorization matched");
            doAuthorization(request, response, chain);
        }
        else {
            logger.trace("doAuthorization un-matched, skip it");
            chain.doFilter(request, response);
        }
    }

    private void doAuthorization(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.trace("doAuthorization start");
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

            authorizationManager.authorize(new UrlAccessRequest(authentication, url, httpMethod));
        } catch (Exception e) {
            logger.error(e, e);
            authorizationFailureHandler.handle(request, response, e);
            return;
        }
        chain.doFilter(request, response);
    }

    protected boolean isUrlProcessingMatched(HttpServletRequest request, HttpServletResponse response) {
        if (ignoredUrlRequestMatcher != null) {
            if (ignoredUrlRequestMatcher.matches(request)) {
                return false;
            }
        }
        return urlRequestMatcher.matches(request);
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return this.featureConfigurer.isEnabled(AuthenticationFeature.CORS_SUPPORT) &&
                "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    public void afterPropertiesSet() {
        logger.trace("afterPropertiesSet");

        Assert.notNull(featureConfigurer, "featureConfigurer must be specified");
        Assert.notNull(authorizationManager, "authorizationManager must be specified");
    }

}

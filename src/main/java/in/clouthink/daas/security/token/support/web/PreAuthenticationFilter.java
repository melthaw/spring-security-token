package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.core.*;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

public class PreAuthenticationFilter extends GenericFilterBean implements Ordered {

    private static final Log logger = LogFactory.getLog(PreAuthenticationFilter.class);

    //@since 1.2.0
    private int order = Ordered.HIGHEST_PRECEDENCE + 3;

    private TokenResolver tokenResolver = new BearerAuthorizationHeaderTokenResolver();

    private AuthorizationFailureHandler authorizationFailureHandler = new DefaultAuthorizationFailureHandler();

    private RequestMatcher urlRequestMatcher;

    private AuthenticationManager authenticationManager;

    //@since 1.6.0
    private FeatureConfigurer featureConfigurer;

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

    public void setFeatureConfigurer(FeatureConfigurer featureConfigurer) {
        this.featureConfigurer = featureConfigurer;
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
    public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        logger.trace("doFilter start");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (isUrlProcessingMatched(request, response) && !isPreFlightRequest(request)) {
            logger.trace("doPreAuthentication matched");
            doPreAuthentication(request, response, chain);
        }
        else {
            logger.trace("doPreAuthentication un-matched, skip it");
            chain.doFilter(request, response);
        }
    }

    private void doPreAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.trace("doPreAuthentication start");
        try {
            String tokenValue = null;
            try {
                tokenValue = tokenResolver.resolve(request, response);
            } catch (Exception e) {
                logger.trace("tokenResolver#resolve failed");
                if (this.featureConfigurer.isEnabled(AuthenticationFeature.STRICT_TOKEN)) {
                    logger.error(e, e);
                    authorizationFailureHandler.handle(request, response, e);
                    return;
                }
            }

            if (tokenValue == null) {
                logger.trace("token is null, skip authenticating token");
                chain.doFilter(request, response);
                return;
            }

            try {
                Authentication authentication = authenticationManager.login(new TokenAuthenticationRequest(tokenValue));
                SecurityContextManager.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error(e, e);
                if (this.featureConfigurer.isDisabled(AuthenticationFeature.IGNORE_PRE_AUTHN_ERROR)) {
                    authorizationFailureHandler.handle(request, response, e);
                    return;
                }
            }
            chain.doFilter(request, response);
        } finally {
            SecurityContextManager.clearContext();
        }
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return this.featureConfigurer.isEnabled(AuthenticationFeature.CORS_SUPPORT) &&
                "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    protected boolean isUrlProcessingMatched(HttpServletRequest request, HttpServletResponse response) {
        return urlRequestMatcher.matches(request);
    }

    @Override
    public void afterPropertiesSet() {
        logger.trace("afterPropertiesSet");

        Assert.notNull(featureConfigurer, "featureConfigurer must be specified");
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
    }

}

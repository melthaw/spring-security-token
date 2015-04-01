package in.clouthink.daas.security.token.core.acl;

/**
 */
public interface UrlMatcher {
    
    boolean matches(String httpMethod, String url);
    
}

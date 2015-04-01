package in.clouthink.daas.security.token.core.acl;

import in.clouthink.daas.security.token.core.Authentication;

public class UrlAccessRequest implements AccessRequest {
    
    private Authentication authentication;
    
    private String url;
    
    private String httpMethod;
    
    public UrlAccessRequest(Authentication authentication,
                            String url,
                            String httpMethod) {
        this.authentication = authentication;
        this.url = url;
        this.httpMethod = httpMethod;
    }
    
    @Override
    public Authentication getAuthentication() {
        return authentication;
    }
    
    @Override
    public String getTarget() {
        return url;
    }
    
    @Override
    public String getOperation() {
        return httpMethod;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getHttpMethod() {
        return httpMethod;
    }
    
}

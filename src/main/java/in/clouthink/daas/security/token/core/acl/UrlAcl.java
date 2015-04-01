package in.clouthink.daas.security.token.core.acl;

/**
 */
public abstract class UrlAcl implements Acl {
    
    String urlRealm;
    
    String[] httpMethods = new String[] { HttpMethod.ALL };
    
    String[] grantRules = new String[] {};
    
    public UrlAcl(String urlRealm, String... grantRules) {
        this.urlRealm = urlRealm;
        this.grantRules = grantRules;
    }
    
    public UrlAcl(String urlRealm, String[] httpMethods, String... grantRules) {
        this.urlRealm = urlRealm;
        this.httpMethods = httpMethods;
        this.grantRules = grantRules;
    }
    
    @Override
    public String getRealm() {
        return urlRealm;
    }
    
    @Override
    public String[] getActions() {
        return httpMethods;
    }
    
    public String getUrlRealm() {
        return urlRealm;
    }
    
    public String[] getHttpMethods() {
        return httpMethods;
    }
    
    @Override
    public String[] getGrantRules() {
        return grantRules;
    }
    
}

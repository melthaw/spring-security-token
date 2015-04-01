package in.clouthink.daas.security.token.core.acl;

/**
 */
public class AntPathUrlAcl extends UrlAcl {
    
    private AntPathUrlMatcher matcher;
    
    public AntPathUrlAcl(String urlRealm,
                         boolean caseSensitive,
                         String... grantRules) {
        super(urlRealm, grantRules);
        this.matcher = new AntPathUrlMatcher(urlRealm,
                                             HttpMethod.ALL,
                                             caseSensitive);
    }
    
    public AntPathUrlAcl(String urlRealm,
                         String[] httpMethods,
                         boolean caseSensitive,
                         String... grantRules) {
        super(urlRealm, httpMethods, grantRules);
        this.matcher = new AntPathUrlMatcher(urlRealm,
                                             httpMethods,
                                             caseSensitive);
    }
    
    @Override
    public boolean matches(String target, String action) {
        return matcher.matches(target, action);
    }
}

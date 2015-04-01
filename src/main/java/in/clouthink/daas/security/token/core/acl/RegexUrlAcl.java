package in.clouthink.daas.security.token.core.acl;

/**
 */
public class RegexUrlAcl extends UrlAcl {
    
    private RegexUrlMatcher matcher;
    
    public RegexUrlAcl(String urlRealm,
                       boolean caseSensitive,
                       String... grantRules) {
        super(urlRealm, grantRules);
        this.matcher = new RegexUrlMatcher(urlRealm,
                                           HttpMethod.ALL,
                                           caseSensitive);
    }
    
    public RegexUrlAcl(String urlRealm,
                       String[] httpMethods,
                       boolean caseSensitive,
                       String... grantRules) {
        super(urlRealm, httpMethods, grantRules);
        this.matcher = new RegexUrlMatcher(urlRealm, httpMethods, caseSensitive);
    }
    
    @Override
    public boolean matches(String target, String action) {
        return matcher.matches(target, action);
    }
}

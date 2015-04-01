package in.clouthink.daas.security.token.core.acl;

/**
 */
public class MatchAllUrlAcl extends UrlAcl {
    
    public MatchAllUrlAcl() {
        super("/**");
    }
    
    @Override
    public boolean matches(String target, String action) {
        return true;
    }
    
}

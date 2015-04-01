package in.clouthink.daas.security.token.core.acl;

public interface Acl {
    
    public String getRealm();
    
    public String[] getActions();
    
    public String[] getGrantRules();
    
    public boolean matches(String target, String action);
    
}

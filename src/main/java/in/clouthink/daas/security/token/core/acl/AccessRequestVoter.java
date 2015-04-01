package in.clouthink.daas.security.token.core.acl;

public interface AccessRequestVoter<T extends AccessRequest> {
    
    public AccessResponse vote(T t, String grantRule);
    
}

package in.clouthink.daas.security.token.core.acl;

/**
 */
public abstract class AbstractAccessRequestVoter<T extends AccessRequest> implements
                                                                          AccessRequestVoter<T> {
    
    private boolean caseInsensitive = false;
    
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }
    
    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }
    
}

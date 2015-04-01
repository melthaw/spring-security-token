package in.clouthink.daas.security.token.core.acl;

import in.clouthink.daas.security.token.core.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 */
public class AccessRequestUserVoter<T extends AccessRequest> extends
                                                             AbstractAccessRequestVoter<T> {
    
    private static final Log logger = LogFactory.getLog(AccessRequestRoleVoter.class);
    
    public AccessResponse vote(T t, String grantRule) {
        AccessResponse result = doVote(t, grantRule);
        logger.debug("Vote result:" + result);
        return result;
    }
    
    private AccessResponse doVote(T t, String grantRule) {
        if (isMatched(grantRule)) {
            String rule = getRuleBody(grantRule);
            User user = t.getAuthentication().currentToken().getOwner();
            if (isCaseInsensitive()) {
                if (user.getUsername().equalsIgnoreCase(rule)) {
                    return AccessResponse.APPROVED;
                }
            }
            else {
                if (user.getUsername().equals(rule)) {
                    return AccessResponse.APPROVED;
                }
            }
            
        }
        return AccessResponse.ABANDON;
    }
    
    boolean isMatched(String grantRule) {
        return grantRule.startsWith("USERNAME:");
    }
    
    String getRuleBody(String grantRule) {
        return grantRule.substring("USERNAME:".length());
    }
    
}

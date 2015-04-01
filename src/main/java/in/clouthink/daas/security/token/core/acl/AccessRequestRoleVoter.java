package in.clouthink.daas.security.token.core.acl;

import in.clouthink.daas.security.token.core.Role;
import in.clouthink.daas.security.token.core.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 */
public class AccessRequestRoleVoter<T extends AccessRequest> extends
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
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (isCaseInsensitive()) {
                    if (role.getName().equalsIgnoreCase(rule)) {
                        return AccessResponse.APPROVED;
                    }
                }
                else {
                    if (role.getName().equals(rule)) {
                        return AccessResponse.APPROVED;
                    }
                }
            }
        }
        return AccessResponse.ABANDON;
    }
    
    boolean isMatched(String grantRule) {
        return grantRule.startsWith("ROLE:");
    }
    
    String getRuleBody(String grantRule) {
        return grantRule.substring("ROLE:".length());
    }
    
}

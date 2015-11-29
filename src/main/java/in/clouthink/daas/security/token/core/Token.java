package in.clouthink.daas.security.token.core;

import java.io.Serializable;
import java.util.Date;

public interface Token extends Serializable {
    
    /**
     * @return the value of token
     */
    String getToken();
    
    /**
     * @return the owner of token
     */
    User getOwner();
    
    /**
     * @return the expired time of token
     */
    Date getExpiredDate();
    
    /**
     * @return the latest active time of token (user accessed the protected
     *         resource with the token)
     */
    Date getLatestTime();
    
    /**
     * @return true the user can access the protected resource , false the user
     *         can't access the protected resource ,and once the user access the
     *         protected resource with the disabled token ,the token will be
     *         revoked immediately
     */
    boolean isEnabled();
    
    /**
     * disable the token but not revoke the token (when the user access the
     * protected resource with the disabled token , it will tell user the token
     * is disabled, in another words, the user is kicked off )
     */
    void disable();
    
    /**
     * @param timeout
     *            update the expired time if user accessed the protected
     *            resource , always add the max live duration based on current
     *            timestamp
     */
    void updateExpiredDate(long timeout);
    
}

package in.clouthink.daas.security.token.core;

import java.io.Serializable;
import java.util.Date;

public interface Token extends Serializable {
    
    public String getToken();
    
    public User getOwner();
    
    public Date getExpiredDate();
    
    public Date getLatestTime();
    
}

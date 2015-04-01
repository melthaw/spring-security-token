package in.clouthink.daas.security.token.core;

import java.io.Serializable;
import java.util.List;

public interface User extends Serializable {
    
    public String getId();
    
    public String getUsername();
    
    public String getPassword();
    
    public boolean isEnabled();
    
    public boolean isExpired();
    
    public boolean isLocked();
    
    public List<Role> getRoles();
    
}

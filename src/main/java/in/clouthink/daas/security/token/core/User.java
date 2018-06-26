package in.clouthink.daas.security.token.core;

import java.io.Serializable;
import java.util.List;

public interface User extends Serializable {
    
    String getId();
    
    String getUsername();
    
    String getPassword();
    
    boolean isEnabled();
    
    boolean isExpired();
    
    boolean isLocked();
    
    List<Role> getRoles();

}

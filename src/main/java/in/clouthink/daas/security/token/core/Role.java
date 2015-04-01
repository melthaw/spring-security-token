package in.clouthink.daas.security.token.core;

import java.io.Serializable;

public interface Role extends Serializable {
    
    public static final Role DUMMY_ROLE = new Role() {
        @Override
        public String getName() {
            return "DUMMY";
        }
    };
    
    public String getName();
    
}

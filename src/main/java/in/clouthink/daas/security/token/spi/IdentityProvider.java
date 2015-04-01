package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.User;

public interface IdentityProvider<T extends User> {
    
    public T findByUsername(String username);
    
}

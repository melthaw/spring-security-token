package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;

import java.util.List;

public interface TokenProvider<T extends Token> {
    
    public void saveToken(T token);
    
    public T findByToken(String token);
    
    public void revokeToken(T token);
    
    public List<T> findByUser(User user);
    
}

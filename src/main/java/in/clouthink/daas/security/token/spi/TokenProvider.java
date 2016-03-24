package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;

import java.util.List;

public interface TokenProvider<T extends Token> {
    
    void saveToken(T token);
    
    T findByToken(String token);
    
    void revokeToken(T token);
    
    List<T> findByUser(User user);
    
}

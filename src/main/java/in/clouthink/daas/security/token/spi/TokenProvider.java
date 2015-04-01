package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Token;

public interface TokenProvider<T extends Token> {
    
    public void saveToken(T token);
    
    public T findByToken(String token);
    
    public void revokeToken(T token);
    
}

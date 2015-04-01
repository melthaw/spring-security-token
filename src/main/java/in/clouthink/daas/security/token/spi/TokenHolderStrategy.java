package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.Token;

public interface TokenHolderStrategy {
    
    void clearContext();
    
    Token getCurrent();
    
    void setCurrent(Token token);
    
}

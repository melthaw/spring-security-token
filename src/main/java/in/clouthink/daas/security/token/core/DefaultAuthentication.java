package in.clouthink.daas.security.token.core;

/**
 */
public class DefaultAuthentication implements Authentication {
    
    private Token token;
    
    public DefaultAuthentication(Token token) {
        this.token = token;
    }
    
    @Override
    public Token currentToken() {
        return token;
    }
    
}

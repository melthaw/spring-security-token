package in.clouthink.daas.security.token.core;

/**
 */
public class AnonymousAuthentication implements Authentication {
    
    @Override
    public Token currentToken() {
        return null;
    }
    
}

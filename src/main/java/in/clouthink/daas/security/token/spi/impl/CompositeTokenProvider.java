package in.clouthink.daas.security.token.spi.impl;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.spi.TokenProvider;

public class CompositeTokenProvider implements TokenProvider<Token> {
    
    private TokenProvider frontProvider;
    
    private TokenProvider backProvider;
    
    public CompositeTokenProvider(TokenProvider frontProvider,
                                  TokenProvider backProvider) {
        this.frontProvider = frontProvider;
        this.backProvider = backProvider;
    }
    
    @Override
    public void saveToken(Token token) {
        if (token == null) {
            return;
        }
        frontProvider.saveToken(token);
        backProvider.saveToken(token);
    }
    
    @Override
    public Token findByToken(String token) {
        Token result = frontProvider.findByToken(token);
        if (result != null) {
            return result;
        }
        result = backProvider.findByToken(token);
        if (result != null) {
            frontProvider.saveToken(result);
            return result;
        }
        return result;
    }
    
    @Override
    public void revokeToken(Token token) {
        if (token == null) {
            return;
        }
        frontProvider.revokeToken(token);
        backProvider.revokeToken(token);
    }
}

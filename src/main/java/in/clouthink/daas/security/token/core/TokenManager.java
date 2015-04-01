package in.clouthink.daas.security.token.core;

public interface TokenManager extends TokenLifeSupport{
    
    public Token createToken(User owner);
    
    public Token findToken(String token);
    
    public void revokeToken(String token);
    
}

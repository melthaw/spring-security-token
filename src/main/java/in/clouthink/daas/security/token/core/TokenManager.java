package in.clouthink.daas.security.token.core;

public interface TokenManager extends TokenLifeSupport {

    void refreshToken(Token token);

    Token createToken(User owner);

    Token findToken(String token);

    void revokeToken(String token);

}

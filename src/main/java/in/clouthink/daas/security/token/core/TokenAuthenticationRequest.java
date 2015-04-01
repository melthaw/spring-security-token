package in.clouthink.daas.security.token.core;

/**
 */
public class TokenAuthenticationRequest implements AuthenticationRequest {
    
    private String token;
    
    public TokenAuthenticationRequest(String token) {
        this.token = token;
    }
    
    @Override
    public String getPrincipal() {
        return token;
    }
    
    @Override
    public String getCredentials() {
        return null;
    }
    
}

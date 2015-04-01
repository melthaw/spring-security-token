package in.clouthink.daas.security.token.core;

/**
 */
public class UsernamePasswordAuthenticationRequest implements
                                                  AuthenticationRequest {
    
    private String username;
    
    private String password;
    
    public UsernamePasswordAuthenticationRequest(String username,
                                                 String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String getPrincipal() {
        return username;
    }
    
    @Override
    public String getCredentials() {
        return password;
    }
}

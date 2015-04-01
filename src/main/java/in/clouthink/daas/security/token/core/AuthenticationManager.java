package in.clouthink.daas.security.token.core;

public interface AuthenticationManager {
    
    public Authentication login(AuthenticationRequest request);
    
    public void logout(Authentication authentication);
    
}

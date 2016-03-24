package in.clouthink.daas.security.token.core;

public interface AuthenticationManager {
    
    Authentication login(AuthenticationRequest request);
    
    void logout(Authentication authentication);
    
}

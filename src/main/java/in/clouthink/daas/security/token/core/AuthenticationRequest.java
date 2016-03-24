package in.clouthink.daas.security.token.core;

public interface AuthenticationRequest {
    
    Object getPrincipal();
    
    Object getCredentials();
    
}

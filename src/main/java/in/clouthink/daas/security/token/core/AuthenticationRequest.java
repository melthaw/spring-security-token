package in.clouthink.daas.security.token.core;

public interface AuthenticationRequest {
    
    public Object getPrincipal();
    
    public Object getCredentials();
    
}

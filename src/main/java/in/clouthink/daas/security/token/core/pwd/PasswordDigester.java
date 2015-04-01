package in.clouthink.daas.security.token.core.pwd;

/**
 */
public interface PasswordDigester {
    
    public String encode(String rawPassword, String salt);
    
    public boolean matches(String rawPassword,
                           String encodedPassword,
                           String salt);
    
}

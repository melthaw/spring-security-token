package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.pwd.PasswordDigester;

public interface PasswordDigesterProvider {
    
    PasswordDigester getPasswordDigester(String digestAlgorithm);
    
}

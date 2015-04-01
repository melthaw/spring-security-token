package in.clouthink.daas.security.token.spi.impl.memory;

import java.util.Arrays;
import java.util.List;

import in.clouthink.daas.security.token.core.Role;
import in.clouthink.daas.security.token.core.User;
import in.clouthink.daas.security.token.core.pwd.PasswordDigester;
import in.clouthink.daas.security.token.spi.IdentityProvider;
import in.clouthink.daas.security.token.spi.PasswordDigesterProvider;
import in.clouthink.daas.security.token.spi.impl.DefaultPasswordDigesterProvider;

/**
 */
public class IdentityProviderMemoryImpl implements IdentityProvider<User> {
    
    private PasswordDigesterProvider passwordDigesterProvider = new DefaultPasswordDigesterProvider();
    
    @Override
    public User findByUsername(final String username) {
        return new User() {
            @Override
            public String getId() {
                return username;
            }
            
            @Override
            public String getUsername() {
                return username;
            }
            
            @Override
            public String getPassword() {
                PasswordDigester passwordDigester = passwordDigesterProvider.getPasswordDigester("MD5");
                return passwordDigester.encode(username, null);
            }
            
            @Override
            public boolean isEnabled() {
                return true;
            }
            
            @Override
            public boolean isExpired() {
                return false;
            }
            
            @Override
            public boolean isLocked() {
                return false;
            }
            
            @Override
            public List<Role> getRoles() {
                return Arrays.asList(Role.DUMMY_ROLE);
            }
        };
    }
}

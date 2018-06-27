package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.LoginAttempt;
import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;

import java.util.List;

/**
 * @since 1.7.0
 */
public interface LoginAttemptProvider<T extends LoginAttempt> {

    void saveLoginAttempt(T loginAttempt);

    T findByUsername(String username);

    void revokeLoginAttempt(T loginAttempt);

}

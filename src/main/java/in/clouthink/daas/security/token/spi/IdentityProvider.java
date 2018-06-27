package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.User;

public interface IdentityProvider<T extends User> {

    /**
     * @param username
     * @return
     */
    T findByUsername(String username);

    /**
     * Lock the user by username
     *
     * @param username
     * @return
     * @throws in.clouthink.daas.security.token.exception.UserNotFoundException if the user with the specified username not found
     * @since 1.7.0
     */
    T lock(String username);

}

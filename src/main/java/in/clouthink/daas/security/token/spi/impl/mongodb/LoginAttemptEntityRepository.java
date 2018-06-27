package in.clouthink.daas.security.token.spi.impl.mongodb;

import in.clouthink.daas.security.token.spi.impl.model.LoginAttemptEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The mongodb repository for login attempt entity
 *
 * @since 1.7.0
 */
public interface LoginAttemptEntityRepository extends PagingAndSortingRepository<LoginAttemptEntity, String> {

    LoginAttemptEntity findFirstByUsername(String username);

}

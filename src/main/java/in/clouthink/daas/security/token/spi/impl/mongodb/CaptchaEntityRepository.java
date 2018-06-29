package in.clouthink.daas.security.token.spi.impl.mongodb;

import in.clouthink.daas.security.token.spi.impl.model.CaptchaEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * The mongodb repository for captcha entity
 *
 * @since 1.8.0
 */
public interface CaptchaEntityRepository extends PagingAndSortingRepository<CaptchaEntity, String> {

}

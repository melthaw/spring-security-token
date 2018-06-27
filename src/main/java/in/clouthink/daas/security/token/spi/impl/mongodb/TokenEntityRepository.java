package in.clouthink.daas.security.token.spi.impl.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import in.clouthink.daas.security.token.spi.impl.model.TokenEntity;

/**
 * The mongodb repository for token entity
 */
public interface TokenEntityRepository extends PagingAndSortingRepository<TokenEntity, String> {

    TokenEntity findFirstByToken(String token);

    @Query("{'owner.$id':?0}")
    List<TokenEntity> findByOwnerId(String userId);

}

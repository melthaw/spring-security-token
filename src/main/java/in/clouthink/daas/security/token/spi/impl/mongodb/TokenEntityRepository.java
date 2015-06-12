package in.clouthink.daas.security.token.spi.impl.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import in.clouthink.daas.security.token.spi.impl.model.TokenEntity;

/**
 */
public interface TokenEntityRepository extends
                                      PagingAndSortingRepository<TokenEntity, String> {
    
    public TokenEntity findByToken(String token);
    
    @Query("{'owner.$id':?0}")
    public List<TokenEntity> findByOwnerId(String userId);
    
}

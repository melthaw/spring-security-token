package in.clouthink.daas.security.token.spi.impl.mongodb;

import in.clouthink.daas.security.token.spi.impl.model.TokenEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 */
public interface TokenEntityRepository extends
                                      PagingAndSortingRepository<TokenEntity, String> {
    
    public TokenEntity findByToken(String token);
    
}

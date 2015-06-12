package in.clouthink.daas.security.token.spi.impl.mongodb;

import in.clouthink.daas.security.token.core.User;
import in.clouthink.daas.security.token.spi.TokenProvider;
import in.clouthink.daas.security.token.spi.impl.model.TokenEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 */
public class TokenProviderMongodbImpl implements TokenProvider<TokenEntity> {
    
    public static final Log logger = LogFactory.getLog(TokenProviderMongodbImpl.class);
    
    @Autowired
    private TokenEntityRepository tokenEntityRepository;
    
    @Override
    public void saveToken(TokenEntity token) {
        logger.debug(String.format("Put token:%s expiredAt:%s",
                                   token.getToken(),
                                   token.getExpiredDate()));
        if (token == null) {
            return;
        }
        tokenEntityRepository.save(token);
    }
    
    @Override
    public TokenEntity findByToken(String token) {
        logger.debug(String.format("Get token:%s", token));
        return tokenEntityRepository.findByToken(token);
    }
    
    @Override
    public void revokeToken(TokenEntity token) {
        logger.debug(String.format("Del token:%s", token));
        if (token == null) {
            return;
        }
        tokenEntityRepository.delete(token);
    }
    
    @Override
    public List<TokenEntity> findByUser(User user) {
        if (user == null) {
            return null;
        }
        return tokenEntityRepository.findByOwnerId(user.getId());
    }
    
}

package in.clouthink.daas.security.token.spi.impl.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.spi.TokenProvider;

/**
 */
public class TokenProviderRedisImpl implements TokenProvider<Token> {
    
    public static final Log logger = LogFactory.getLog(TokenProviderRedisImpl.class);
    
    @Autowired
    private RedisTemplate<String, Token> redisTemplate;
    
    @Override
    public void saveToken(Token token) {
        logger.debug(String.format("Put T:%s expiredAt:%s",
                                   token.getToken(),
                                   token.getExpiredDate()));
        redisTemplate.opsForHash().put("T:" + token.getToken(),
                                       token.getToken(),
                                       token);
        redisTemplate.expireAt("T:" + token.getToken(),
                               token.getExpiredDate());
    }
    
    @Override
    public Token findByToken(String token) {
        logger.debug(String.format("Get T:%s", token));
        return (Token) redisTemplate.opsForHash().get("T:" + token, token);
    }
    
    @Override
    public void revokeToken(Token token) {
        logger.debug(String.format("Del T:%s", token));
        if (token == null) {
            return;
        }
        redisTemplate.opsForHash().delete("T:" + token.getToken(),
                                          token.getToken());
    }
}

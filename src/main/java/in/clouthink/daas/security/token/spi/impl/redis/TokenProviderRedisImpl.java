package in.clouthink.daas.security.token.spi.impl.redis;

import in.clouthink.daas.security.token.core.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.spi.TokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The redis crud for token.
 */
public class TokenProviderRedisImpl implements TokenProvider<Token> {

    public static final Log logger = LogFactory.getLog(TokenProviderRedisImpl.class);

    @Autowired
    private RedisTemplate<String, Token> redisTemplateToken;

    @Autowired
    private RedisTemplate<String, String> redisTemplateSet;

    @Override
    public void saveToken(Token token) {
        logger.debug(String.format("Put TOKEN:%s expiredAt:%s",
                                   token.getToken(),
                                   token.getExpiredDate()));
        // put the token to cache
        redisTemplateToken.opsForHash().put("TOKEN:" + token.getToken(),
                                            token.getToken(),
                                            token);
        redisTemplateToken.expireAt("TOKEN:" + token.getToken(),
                                    token.getExpiredDate());
        // maintain the user & token relationship
        if (token.getOwner() == null) {
            return;
        }
        redisTemplateSet.opsForSet().add("USER:" + token.getOwner().getUsername(),
                                         token.getToken());
        // update the expired date
        redisTemplateToken.expireAt("USER:" + token.getOwner().getUsername(),
                                    token.getExpiredDate());
    }

    @Override
    public Token findByToken(String token) {
        logger.debug(String.format("Get TOKEN:%s", token));
        return (Token) redisTemplateToken.opsForHash().get("TOKEN:" + token, token);
    }

    @Override
    public void revokeToken(Token token) {
        logger.debug(String.format("Del TOKEN:%s", token));
        if (token == null) {
            return;
        }
        redisTemplateToken.opsForHash().delete("TOKEN:" + token.getToken(),
                                               token.getToken());
        // remove the user & token relationship
        if (token.getOwner() == null) {
            return;
        }
        redisTemplateSet.opsForSet()
                        .remove("USER:" + token.getOwner().getUsername(),
                                token.getToken());
    }

    @Override
    public List<Token> findByUser(User user) {
        if (user == null) {
            return null;
        }
        List<Token> result = new ArrayList<Token>();

        Set<String> tokens = redisTemplateSet.opsForSet()
                                             .members("USER:"
                                                              + user.getUsername());
        for (String token : tokens) {
            Token t = findByToken(token);
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

}

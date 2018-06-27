package in.clouthink.daas.security.token.spi.impl.memcached;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;
import in.clouthink.daas.security.token.spi.TokenProvider;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 */
public class TokenProviderMemcachedImpl implements TokenProvider<Token> {

    public static final Log logger = LogFactory.getLog(TokenProviderMemcachedImpl.class);

    @Autowired
    private MemcachedClient memcachedClient;

    @Override
    public void saveToken(Token token) {
        logger.debug(String.format("Put token:%s expiredAt:%s",
                                   token.getToken(),
                                   token.getExpiredDate()));
        memcachedClient.add("T_" + token.getToken(),
                            (int) ((token.getExpiredDate().getTime() - System.currentTimeMillis()) / 1000),
                            token);
    }

    @Override
    public Token findByToken(String token) {
        logger.debug(String.format("Get token:%s", token));
        return (Token) memcachedClient.get("T_" + token);
    }

    @Override
    public void revokeToken(Token token) {
        logger.debug(String.format("Del token:%s", token));
        if (token == null) {
            return;
        }
        memcachedClient.delete("T_" + token.getToken());
    }

    @Override
    public List<Token> findByUser(User user) {
        // TODO : NOT SUPPORTED NOW
        return Collections.EMPTY_LIST;
    }

}

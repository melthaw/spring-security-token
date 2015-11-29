package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.exception.AuthenticationRequiredException;
import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by LiangBin on 15/11/29.
 */
public class BearerAuthorizationHeaderTokenResolver implements TokenResolver {
    
    private static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    
    @Override
    public String resolve(HttpServletRequest request,
                          HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authHeader)) {
            throw new AuthenticationRequiredException();
        }
        
        if (authHeader.length() <= HEADER_AUTHORIZATION_PREFIX.length()) {
            throw new IllegalArgumentException("Unrecognized Authorization header.");
        }
        
        String base64Final = authHeader.substring(HEADER_AUTHORIZATION_PREFIX.length());
        String tokenValue = null;
        try {
            tokenValue = new String(Base64.decode(base64Final.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unrecognized Authorization header.");
        }
        
        return tokenValue;
    }
    
}

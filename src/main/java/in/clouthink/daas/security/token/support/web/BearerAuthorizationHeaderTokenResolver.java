package in.clouthink.daas.security.token.support.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;

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
            return null;
        }
        
        if (authHeader.length() <= HEADER_AUTHORIZATION_PREFIX.length()) {
            throw new IllegalArgumentException("Unrecognized Authorization header.");
        }
        
        String base64Final = authHeader.substring(HEADER_AUTHORIZATION_PREFIX.length());
        try {
            return new String(Base64.decode(base64Final.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unrecognized Authorization header.");
        }
    }
    
}

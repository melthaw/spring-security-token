package in.clouthink.daas.security.token.support.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import in.clouthink.daas.security.token.exception.AuthenticationRequiredException;
import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;

/**
 *
 */
public class CookieTokenResolver implements TokenResolver {
    
    private static final String COOKIE_KEY = "dass-token";
    
    @Override
    public String resolve(HttpServletRequest request,
                          HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies == null || cookies.length == 0) {
            throw new AuthenticationRequiredException();
        }
        
        for (Cookie cookie : cookies) {
            String cookieName = cookie.getName();
            if (COOKIE_KEY.equals(cookieName)) {
                String cookieValue = cookie.getValue();
                if (StringUtils.isEmpty(cookieValue)) {
                    throw new AuthenticationRequiredException();
                }
                
                try {
                    return new String(Base64.decode(cookieValue.getBytes("UTF-8")));
                }
                catch (UnsupportedEncodingException e) {
                    throw new IllegalArgumentException("Unrecognized Authorization header.");
                }
            }
        }
        
        throw new AuthenticationRequiredException();
    }
    
}

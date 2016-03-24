package in.clouthink.daas.security.token.spi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public interface PreLoginHandler {
    
    void handle(HttpServletRequest request, HttpServletResponse response);
}

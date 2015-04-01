package in.clouthink.daas.security.token.support.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationFailureHandler {
    
    void handle(HttpServletRequest request,
                HttpServletResponse response,
                Exception exception) throws IOException, ServletException;
    
}

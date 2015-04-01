package in.clouthink.daas.security.token.support.web;

import org.springframework.http.HttpEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
public interface HttpResponseRenderer {
    
    public void handleResponse(HttpEntity<?> httpEntity,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException;
    
}

package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
public class DefaultAuthenticationSuccessHandler implements
                                                AuthenticationSuccessHandler {
    
    private HttpResponseRenderer renderer = new HttpResponseJsonRenderer();
    
    public HttpResponseRenderer getRenderer() {
        return renderer;
    }
    
    public void setRenderer(HttpResponseRenderer renderer) {
        this.renderer = renderer;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException,
                                                                      ServletException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cache-Control", "no-store");
            headers.set("Pragma", "no-cache");
            String token = authentication.currentToken().getToken();
            ResponseEntity result = new ResponseEntity(WebResultWrapper.succeedMap("token",
                                                                                   token),
                                                       headers,
                                                       HttpStatus.OK);
            renderer.handleResponse(result, request, response);
            response.flushBuffer();
        }
        catch (IOException e) {
            throw e;
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            // Wrap other Exceptions. These are not expected to happen
            throw new ServletException(e);
        }
    }
}

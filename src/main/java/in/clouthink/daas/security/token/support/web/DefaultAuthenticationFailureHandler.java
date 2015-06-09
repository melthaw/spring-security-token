package in.clouthink.daas.security.token.support.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import org.springframework.http.ResponseEntity;

/**
 */
public class DefaultAuthenticationFailureHandler implements
                                                AuthenticationFailureHandler {
    
    private WebResponseExceptionTranslator translater;
    
    private HttpResponseRenderer renderer = new HttpResponseJsonRenderer();
    
    public DefaultAuthenticationFailureHandler() {
        translater = new DefaultWebResponseExceptionTranslator();
    }
    
    public DefaultAuthenticationFailureHandler(MessageProvider messageProvider) {
        translater = new DefaultWebResponseExceptionTranslator(messageProvider);
    }
    
    public WebResponseExceptionTranslator getTranslater() {
        return translater;
    }
    
    public void setTranslater(WebResponseExceptionTranslator translater) {
        this.translater = translater;
    }
    
    public HttpResponseRenderer getRenderer() {
        return renderer;
    }
    
    public void setRenderer(HttpResponseRenderer renderer) {
        this.renderer = renderer;
    }
    
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       Exception exception) throws IOException,
                                           ServletException {
        try {
            ResponseEntity result = translater.translate(exception);
            renderer.handleResponse(result, request, response);
            response.flushBuffer();
        }
        catch (ServletException e) {
            throw e;
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

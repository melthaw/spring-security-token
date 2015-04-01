package in.clouthink.daas.security.token.support.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 */
public class HttpResponseJsonRenderer implements HttpResponseRenderer {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void handleResponse(HttpEntity<?> httpEntity,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        if (httpEntity == null) {
            return;
        }
        if (httpEntity instanceof ResponseEntity) {
            response.setStatus(((ResponseEntity<?>) httpEntity).getStatusCode()
                                                               .value());
        }
        // HttpHeaders entityHeaders = httpEntity.getHeaders();
        // if (!entityHeaders.isEmpty()) {
        // response.getHeaders().addAll(entityHeaders.toSingleValueMap());
        // }
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        Object body = httpEntity.getBody();
        if (body != null) {
            objectMapper.writeValue(response.getWriter(), body);
        }
    }
}

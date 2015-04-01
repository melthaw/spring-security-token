package in.clouthink.daas.security.token.support.web;

import org.springframework.http.ResponseEntity;

public interface WebResponseExceptionTranslator {
    
    ResponseEntity<?> translate(Exception e) throws Exception;
    
}

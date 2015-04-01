package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;

public class DefaultWebResponseExceptionTranslator implements
                                                  WebResponseExceptionTranslator {
    
    public ResponseEntity<?> translate(Exception e) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        
        if (e instanceof UserNotFoundException) {
            return new ResponseEntity(WebResultWrapper.failedMap("Invalid username or password."),
                                      headers,
                                      HttpStatus.OK);
        }
        if (e instanceof UserLockedException) {
            return new ResponseEntity(WebResultWrapper.failedMap("The user is locked."),
                                      headers,
                                      HttpStatus.OK);
        }
        if (e instanceof UserExpiredException) {
            return new ResponseEntity(WebResultWrapper.failedMap("The user is expired."),
                                      headers,
                                      HttpStatus.OK);
        }
        if (e instanceof UserDisabledException) {
            return new ResponseEntity(WebResultWrapper.failedMap("The user is disabled."),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof BadCredetialException) {
            return new ResponseEntity(WebResultWrapper.failedMap("Invalid username or password."),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof InvalidTokenException) {
            return new ResponseEntity(WebResultWrapper.failedMap("The token is invalid."),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof TokenExpiredException) {
            return new ResponseEntity(WebResultWrapper.failedMap("The token is expired."),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof AuthenticationFailureException) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = "Authentication failed.";
            }
            return new ResponseEntity(WebResultWrapper.failedMap(message),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof AuthorizationFailureException) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = "Authorization failed.";
            }
            return new ResponseEntity(WebResultWrapper.failedMap(message),
                                      headers,
                                      HttpStatus.UNAUTHORIZED);
        }
        
        if (e instanceof AccessDeniedException) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = "No permission.Access denied.";
            }
            return new ResponseEntity(WebResultWrapper.failedMap(message),
                                      headers,
                                      HttpStatus.FORBIDDEN);
        }
        
        return new ResponseEntity(WebResultWrapper.failedMap(e.getMessage()),
                                  headers,
                                  HttpStatus.OK);
    }
}

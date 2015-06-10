package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.exception.*;
import in.clouthink.daas.security.token.support.i18n.DefaultMessageProvider;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;

public class DefaultWebResponseExceptionTranslator implements
                                                  WebResponseExceptionTranslator {
    
    private MessageProvider messageProvider = new DefaultMessageProvider();
    
    public DefaultWebResponseExceptionTranslator() {
    }
    
    public DefaultWebResponseExceptionTranslator(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }
    
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }
    
    public ResponseEntity<?> translate(Exception e) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        
        if (e instanceof UserNotFoundException) {
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 messageProvider.getMessage(ErrorConstants.INVALID_USER_OR_PASSWORD)),
                                      headers,
                                      HttpStatus.OK);
        }
        if (e instanceof UserLockedException) {
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 messageProvider.getMessage(ErrorConstants.USER_IS_LOCKED)),
                                      headers,
                                      HttpStatus.OK);
        }
        if (e instanceof UserExpiredException) {
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 messageProvider.getMessage(ErrorConstants.USER_IS_EXPIRED)),
                                      headers,
                                      HttpStatus.OK);
        }
        if (e instanceof UserDisabledException) {
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 messageProvider.getMessage(ErrorConstants.USER_IS_DISABLED)),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof BadCredetialException) {
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 messageProvider.getMessage(ErrorConstants.INVALID_USER_OR_PASSWORD)),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof InvalidTokenException) {
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 messageProvider.getMessage(ErrorConstants.INVALID_TOKEN_OR_EXPIRED)),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof TokenExpiredException) {
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 messageProvider.getMessage(ErrorConstants.INVALID_TOKEN_OR_EXPIRED)),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof AuthenticationFailureException) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = messageProvider.getMessage(ErrorConstants.AUTHENTICATION_FAILED);
            }
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 message),
                                      headers,
                                      HttpStatus.OK);
        }
        
        if (e instanceof AuthorizationFailureException) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = messageProvider.getMessage(ErrorConstants.AUTHORIZATION_FAILED);
            }
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 message),
                                      headers,
                                      HttpStatus.UNAUTHORIZED);
        }
        
        if (e instanceof AuthenticationRequiredException) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = messageProvider.getMessage(ErrorConstants.AUTHENTICATION_REQUIRED);
            }
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.UNAUTHORIZED.value(),
                                                                 message),
                                      headers,
                                      HttpStatus.UNAUTHORIZED);
        }
        
        if (e instanceof AccessDeniedException) {
            String message = e.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = messageProvider.getMessage(ErrorConstants.NO_PERMISSION);
            }
            return new ResponseEntity(WebResultWrapper.failedMap(HttpStatus.FORBIDDEN.value(),
                                                                 message),
                                      headers,
                                      HttpStatus.FORBIDDEN);
        }
        
        return new ResponseEntity(WebResultWrapper.failedMap(e.getMessage()),
                                  headers,
                                  HttpStatus.OK);
    }
    
}

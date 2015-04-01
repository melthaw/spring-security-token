package in.clouthink.daas.security.token.support.web;

import in.clouthink.daas.security.token.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationSuccessHandler {
    
    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request
     *            the request which caused the successful authentication
     * @param response
     *            the response
     * @param authentication
     *            the <tt>Authentication</tt> object which was created during
     *            the authentication process.
     */
    void onAuthenticationSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException,
                                                               ServletException;
    
}

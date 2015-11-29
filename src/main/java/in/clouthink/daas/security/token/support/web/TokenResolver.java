package in.clouthink.daas.security.token.support.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by LiangBin on 15/11/29.
 */
public interface TokenResolver {
    
    /**
     * Resolve from request
     * 
     * @param request
     * @param response
     * @return
     */
    String resolve(HttpServletRequest request, HttpServletResponse response);
}

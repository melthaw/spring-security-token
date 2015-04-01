package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.TokenLifeSupport;
import in.clouthink.daas.security.token.support.web.AuthenticationFilter;
import in.clouthink.daas.security.token.support.web.AuthorizationFilter;
import in.clouthink.daas.security.token.support.web.LoginEndpoint;
import in.clouthink.daas.security.token.support.web.LogoutEndpoint;

public interface TokenConfigurer {
    
    public void configure(AuthorizationFilter filter);
    
    public void configure(AuthenticationFilter filter);
    
    public void configure(LoginEndpoint endpoint);
    
    public void configure(LogoutEndpoint endpoint);
    
    public void configure(TokenLifeSupport tokenLifeSupport);
    
    public void configure(UrlAclProviderBuilder builder);
    
}

package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.TokenLifeSupport;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.AuthenticationFilter;
import in.clouthink.daas.security.token.support.web.AuthorizationFilter;
import in.clouthink.daas.security.token.support.web.LoginEndpoint;
import in.clouthink.daas.security.token.support.web.LogoutEndpoint;

/**
 */
public class TokenConfigurerAdapter implements TokenConfigurer {
    
    @Override
    public void configure(MessageProvider messageProvider) {
        
    }
    
    @Override
    public void configure(AuthorizationFilter filter) {
        
    }
    
    @Override
    public void configure(AuthenticationFilter filter) {
        
    }
    
    @Override
    public void configure(LoginEndpoint endpoint) {
        
    }
    
    @Override
    public void configure(LogoutEndpoint endpoint) {
        
    }
    
    @Override
    public void configure(TokenLifeSupport tokenLifeSupport) {
        
    }
    
    @Override
    public void configure(UrlAclProviderBuilder builder) {
        
    }
    
}

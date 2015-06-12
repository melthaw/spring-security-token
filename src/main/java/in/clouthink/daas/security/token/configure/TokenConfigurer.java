package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.TokenLifeSupport;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.AuthenticationFilter;
import in.clouthink.daas.security.token.support.web.AuthorizationFilter;
import in.clouthink.daas.security.token.support.web.LoginEndpoint;
import in.clouthink.daas.security.token.support.web.LogoutEndpoint;

import java.util.Locale;

public interface TokenConfigurer {
    
    /**
     * The default value is Locale#ENGLISH, if the locale is not supported,
     * default value will take effect.
     * 
     * @param messageProvider
     */
    public void configure(MessageProvider messageProvider);

    /**
     *
     * @param filter
     */
    public void configure(AuthorizationFilter filter);
    
    public void configure(AuthenticationFilter filter);
    
    public void configure(LoginEndpoint endpoint);
    
    public void configure(LogoutEndpoint endpoint);
    
    public void configure(TokenLifeSupport tokenLifeSupport);
    
    public void configure(UrlAclProviderBuilder builder);
    
}

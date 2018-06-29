package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.FeatureConfigurer;
import in.clouthink.daas.security.token.core.LoginAttemptOptions;
import in.clouthink.daas.security.token.core.TokenOptions;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.*;

/**
 */
public class TokenConfigurerAdapter implements TokenConfigurer {

    @Override
    public void configure(MessageProvider messageProvider) {

    }

    @Override
    public void configure(TokenAuthenticationFilter filter) {

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
    public void configure(TokenOptions tokenOptions) {

    }


    @Override
    public void configure(LoginAttemptOptions loginAttemptOptions) {

    }

    @Override
    public void configure(UrlAclProviderBuilder builder) {

    }

    @Override
    public void configure(FeatureConfigurer featureConfigurer) {

    }
}

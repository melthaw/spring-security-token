package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.FeatureConfigurer;
import in.clouthink.daas.security.token.core.TokenLifeSupport;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.*;

/**
 */
public class TokenConfigurerAdapter implements TokenConfigurer {

    @Override
    public void configure(MessageProvider messageProvider) {

    }

    @Override
    public void configure(PreAuthenticationFilter filter) {

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

    @Override
    public void configure(FeatureConfigurer featureConfigurer) {

    }
}

package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.FeatureConfigurer;
import in.clouthink.daas.security.token.core.LoginAttemptStrategySupport;
import in.clouthink.daas.security.token.core.TokenLifeSupport;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.*;

public interface TokenConfigurer {

    /**
     * The default value is Locale#ENGLISH, if the locale is not supported,
     * default value will take effect.
     *
     * @param messageProvider
     */
    void configure(MessageProvider messageProvider);

    void configure(PreAuthenticationFilter filter);

    /**
     * @param filter
     */
    void configure(AuthorizationFilter filter);

    void configure(AuthenticationFilter filter);

    void configure(LoginEndpoint endpoint);

    void configure(LogoutEndpoint endpoint);

    void configure(TokenLifeSupport tokenLifeSupport);

    void configure(LoginAttemptStrategySupport loginAttemptStrategySupport);

    void configure(UrlAclProviderBuilder builder);

    void configure(FeatureConfigurer featureConfigurer);

}

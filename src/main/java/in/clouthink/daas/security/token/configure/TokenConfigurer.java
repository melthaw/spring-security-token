package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.CaptchaOptions;
import in.clouthink.daas.security.token.core.FeatureConfigurer;
import in.clouthink.daas.security.token.core.LoginAttemptOptions;
import in.clouthink.daas.security.token.core.TokenOptions;
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

    void configure(TokenAuthenticationFilter filter);

    void configure(AuthorizationFilter filter);

    void configure(AuthenticationFilter filter);

    void configure(LoginEndpoint endpoint);

    void configure(LogoutEndpoint endpoint);

    void configure(UrlAclProviderBuilder builder);

    void configure(FeatureConfigurer featureConfigurer);

    void configure(TokenOptions tokenOptions);

    void configure(LoginAttemptOptions loginAttemptOptions);

    void configure(CaptchaOptions captchaOptions);

}

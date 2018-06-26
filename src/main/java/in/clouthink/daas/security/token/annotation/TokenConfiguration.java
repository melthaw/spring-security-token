package in.clouthink.daas.security.token.annotation;

import in.clouthink.daas.security.token.configure.TokenConfigurer;
import in.clouthink.daas.security.token.configure.TokenConfigurerAdapter;
import in.clouthink.daas.security.token.configure.UrlAclProviderBuilder;
import in.clouthink.daas.security.token.core.*;
import in.clouthink.daas.security.token.core.acl.AccessRequestRoleVoter;
import in.clouthink.daas.security.token.core.acl.AccessRequestUserVoter;
import in.clouthink.daas.security.token.federation.DefaultFederationService;
import in.clouthink.daas.security.token.federation.FederationService;
import in.clouthink.daas.security.token.spi.*;
import in.clouthink.daas.security.token.spi.impl.DefaultUrlAuthorizationProvider;
import in.clouthink.daas.security.token.spi.impl.SimpleFederationProvider;
import in.clouthink.daas.security.token.spi.impl.TokenAuthenticationProvider;
import in.clouthink.daas.security.token.spi.impl.UsernamePasswordAuthenticationProvider;
import in.clouthink.daas.security.token.spi.impl.memory.IdentityProviderMemoryImpl;
import in.clouthink.daas.security.token.spi.impl.memory.TokenProviderMemoryImpl;
import in.clouthink.daas.security.token.support.i18n.DefaultMessageProvider;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Configuration
public class TokenConfiguration implements ImportAware, BeanFactoryAware {

    protected ListableBeanFactory beanFactory;

    protected BeanDefinitionRegistry beanDefinitionRegistry;

    protected AnnotationAttributes enableToken;

    protected TokenConfigurer tokenConfigurer = new TokenConfigurerAdapter();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ListableBeanFactory) beanFactory;
        this.beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
    }

    @Autowired(required = false)
    void setConfigurers(Collection<TokenConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException("Only one TokenConfigurer may exist");
        }
        TokenConfigurer configurer = configurers.iterator().next();
        this.tokenConfigurer = configurer;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableToken = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableToken.class.getName(),
                                                                                               false));
        Assert.notNull(this.enableToken,
                       "@EnableToken is not present on importing class " + importMetadata.getClassName());
    }

    @Bean
    @Autowired
    @DependsOn({"daasDefaultAuthorizationManager", "daasFeatureConfigurer"})
    public AuthorizationFilter daasTokenAuthorizationFilter(AuthorizationManager authorizationManager,
                                                            FeatureConfigurer featureConfigurer,
                                                            MessageProvider messageProvider) {
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.setAuthorizationManager(authorizationManager);
        authorizationFilter.setAuthorizationFailureHandler(new DefaultAuthorizationFailureHandler(messageProvider));
        authorizationFilter.setFeatureConfigurer(featureConfigurer);
        tokenConfigurer.configure(authorizationFilter);
        return authorizationFilter;
    }

    @Bean
    @Autowired
    @DependsOn({"daasDefaultAuthenticationManager", "daasFeatureConfigurer"})
    public AuthenticationFilter daasTokenAuthenticationFilter(AuthenticationManager authenticationManager,
                                                              FeatureConfigurer featureConfigurer,
                                                              MessageProvider messageProvider) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthorizationFailureHandler(new DefaultAuthorizationFailureHandler(messageProvider));
        authenticationFilter.setFeatureConfigurer(featureConfigurer);
        tokenConfigurer.configure(authenticationFilter);
        return authenticationFilter;
    }

    @Bean
    @Autowired
    @DependsOn({"daasDefaultAuthenticationManager", "daasFeatureConfigurer"})
    public PreAuthenticationFilter daasTokenPreAuthenticationFilter(AuthenticationManager authenticationManager,
                                                                    FeatureConfigurer featureConfigurer,
                                                                    MessageProvider messageProvider) {
        PreAuthenticationFilter preAuthenticationFilter = new PreAuthenticationFilter();
        preAuthenticationFilter.setAuthenticationManager(authenticationManager);
        preAuthenticationFilter.setAuthorizationFailureHandler(new DefaultAuthorizationFailureHandler(messageProvider));
        preAuthenticationFilter.setFeatureConfigurer(featureConfigurer);
        tokenConfigurer.configure(preAuthenticationFilter);
        return preAuthenticationFilter;
    }

    @Bean
    @Autowired
    @DependsOn("daasDefaultAuthenticationManager")
    public LogoutEndpoint daasTokenLogoutEndpoint(AuthenticationManager authenticationManager,
                                                  MessageProvider messageProvider) {
        LogoutEndpoint logoutEndpoint = new LogoutEndpoint();
        logoutEndpoint.setAuthenticationManager(authenticationManager);
        logoutEndpoint.setAuthorizationFailureHandler(new DefaultAuthorizationFailureHandler(messageProvider));
        tokenConfigurer.configure(logoutEndpoint);
        return logoutEndpoint;
    }

    @Bean
    @Autowired
    @DependsOn("daasDefaultAuthenticationManager")
    public LoginEndpoint daasTokenLoginEndpoint(AuthenticationManager authenticationManager,
                                                MessageProvider messageProvider) {
        LoginEndpoint loginEndpoint = new LoginEndpoint();
        loginEndpoint.setAuthenticationManager(authenticationManager);
        loginEndpoint.setAuthenticationFailureHandler(new DefaultAuthenticationFailureHandler(messageProvider));
        tokenConfigurer.configure(loginEndpoint);
        return loginEndpoint;
    }

    @Bean
    @Autowired
    @DependsOn({"daasUsernamePasswordAuthenticationProvider", "daasTokenAuthenticationProvider"})
    public AuthenticationManager daasDefaultAuthenticationManager(IdentityProvider identityProvider,
                                                                  TokenManager tokenManager) {
        DefaultAuthenticationManager result = new DefaultAuthenticationManager();
        result.addProvider(daasUsernamePasswordAuthenticationProvider(identityProvider, tokenManager));
        result.addProvider(daasTokenAuthenticationProvider(identityProvider, tokenManager));
        return result;
    }

    @Bean
    @Autowired
    @DependsOn({"daasSimpleFederationProvider", "daasTokenAuthenticationProvider"})
    public FederationService daasDefaultFederationService(IdentityProvider identityProvider,
                                                          TokenManager tokenManager) {
        DefaultFederationService result = new DefaultFederationService();
        result.addProvider(daasSimpleFederationProvider(tokenManager));
        result.addProvider(daasTokenAuthenticationProvider(identityProvider, tokenManager));
        return result;
    }

    @Bean
    @Autowired
    public AuthorizationManager daasDefaultAuthorizationManager(AuthorizationProvider authorizationProvider) {
        DefaultAuthorizationManager result = new DefaultAuthorizationManager();
        result.getProviders().add(authorizationProvider);
        return result;
    }

    @Bean
    @Autowired
    public AuthenticationProvider daasUsernamePasswordAuthenticationProvider(IdentityProvider identityProvider,
                                                                             TokenManager tokenManager) {
        UsernamePasswordAuthenticationProvider result = new UsernamePasswordAuthenticationProvider();
        result.setIdentityProvider(identityProvider);
        result.setTokenManager(tokenManager);
        return result;
    }

    @Bean
    @Autowired
    public FederationProvider daasSimpleFederationProvider(TokenManager tokenManager) {
        SimpleFederationProvider result = new SimpleFederationProvider();
        result.setTokenManager(tokenManager);
        return result;
    }

    @Bean
    @Autowired
    public AuthenticationProvider daasTokenAuthenticationProvider(IdentityProvider identityProvider,
                                                                  TokenManager tokenManager) {
        TokenAuthenticationProvider result = new TokenAuthenticationProvider();
        result.setIdentityProvider(identityProvider);
        result.setTokenManager(tokenManager);
        return result;
    }

    @Bean
    @Autowired
    public AuthorizationProvider daasDefaultUrlAuthorizationProvider(AclProvider aclProvider) {
        DefaultUrlAuthorizationProvider result = new DefaultUrlAuthorizationProvider();
        result.getVoters().add(new AccessRequestRoleVoter());
        result.getVoters().add(new AccessRequestUserVoter());
        result.setProvider(aclProvider);
        return result;
    }

    @Bean
    @Autowired
    public TokenManager daasDefaultTokenManager(TokenProvider tokenProvider) {
        DefaultTokenManager tokenManager = new DefaultTokenManager();
        tokenManager.setTokenProvider(tokenProvider);
        tokenConfigurer.configure(tokenManager);
        return tokenManager;
    }

    @Bean
    public AclProvider daasDefaultUrlAclProvider() {
        UrlAclProviderBuilder urlAclProviderBuilder = UrlAclProviderBuilder.newInstance();
        tokenConfigurer.configure(urlAclProviderBuilder);
        return urlAclProviderBuilder.build();
    }

    @Bean
    public TokenProvider daasDefaultTokenProvider() {
        return new TokenProviderMemoryImpl();
    }

    @Bean
    public IdentityProvider daasDefaultIdentityProvider() {
        return new IdentityProviderMemoryImpl();
    }

    @Bean
    public MessageProvider messageProvider() {
        MessageProvider result = new DefaultMessageProvider();
        tokenConfigurer.configure(result);
        return result;
    }

    @Bean
    public FeatureConfigurer daasFeatureConfigurer() {
        FeatureConfigurer featureConfigurer = new FeatureConfigurer();
        tokenConfigurer.configure(featureConfigurer);
        return featureConfigurer;
    }

}

package in.clouthink.daas.security.token.spi.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import in.clouthink.daas.security.token.core.*;
import in.clouthink.daas.security.token.core.pwd.PasswordDigester;
import in.clouthink.daas.security.token.exception.*;
import in.clouthink.daas.security.token.spi.AuthenticationProvider;
import in.clouthink.daas.security.token.spi.DigestMetadataProvider;
import in.clouthink.daas.security.token.spi.IdentityProvider;
import in.clouthink.daas.security.token.spi.PasswordDigesterProvider;

/**
 */
public class UsernamePasswordAuthenticationProvider implements
        AuthenticationProvider<UsernamePasswordAuthenticationRequest>,
        InitializingBean {

    private PasswordDigesterProvider passwordDigesterProvider = new DefaultPasswordDigesterProvider();

    private IdentityProvider identityProvider;

    private TokenManager tokenManager;

    //@since 1.7.0
    private LoginAttemptManager loginAttemptManager;

    //@since 1.6.0
    private FeatureConfigurer featureConfigurer;

    private DigestMetadataProvider digestMetadataProvider = DigestMetadataProvider.INSTANCE;

    public PasswordDigesterProvider getPasswordDigesterProvider() {
        return passwordDigesterProvider;
    }

    @Autowired(required = false)
    public void setPasswordDigesterProvider(PasswordDigesterProvider passwordDigesterProvider) {
        this.passwordDigesterProvider = passwordDigesterProvider;
    }

    public void setIdentityProvider(IdentityProvider identityProvider) {
        this.identityProvider = identityProvider;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public LoginAttemptManager getLoginAttemptManager() {
        return loginAttemptManager;
    }

    public void setLoginAttemptManager(LoginAttemptManager loginAttemptManager) {
        this.loginAttemptManager = loginAttemptManager;
    }

    public FeatureConfigurer getFeatureConfigurer() {
        return featureConfigurer;
    }

    public void setFeatureConfigurer(FeatureConfigurer featureConfigurer) {
        this.featureConfigurer = featureConfigurer;
    }

    public DigestMetadataProvider getDigestMetadataProvider() {
        return digestMetadataProvider;
    }

    @Autowired(required = false)
    public void setDigestMetadataProvider(DigestMetadataProvider digestMetadataProvider) {
        this.digestMetadataProvider = digestMetadataProvider;
    }

    public IdentityProvider getIdentityProvider() {
        return identityProvider;
    }

    @Override
    public boolean supports(AuthenticationRequest request) {
        return (request instanceof UsernamePasswordAuthenticationRequest);
    }

    @Override
    public Authentication authenticate(UsernamePasswordAuthenticationRequest usernamePasswordAuthenticationRequest) {
        String username = usernamePasswordAuthenticationRequest.getPrincipal();
        String password = usernamePasswordAuthenticationRequest.getCredentials();
        User user = identityProvider.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (user.isLocked()) {
            throw new UserLockedException();
        }
        if (!user.isEnabled()) {
            throw new UserDisabledException();
        }
        if (user.isExpired()) {
            throw new UserExpiredException();
        }
        String encodedPassword = user.getPassword();
        String digestAlgorithm = digestMetadataProvider.getDigestAlgorithm(user);
        String salt = digestMetadataProvider.getSalt(user);

        PasswordDigester passwordDigester = passwordDigesterProvider.getPasswordDigester(digestAlgorithm);

        //correct password
        if (passwordDigester.matches(password, encodedPassword, salt)) {

            if (featureConfigurer.isEnabled(AuthenticationFeature.LOGIN_ATTEMPT_ENABLED)) {
                loginAttemptManager.reset(username);
            }

            Token token = tokenManager.createToken(user);
            return new DefaultAuthentication(token);
        }

        //wrong password
        if (featureConfigurer.isEnabled(AuthenticationFeature.LOGIN_ATTEMPT_ENABLED)) {
            LoginAttempt loginAttempt = loginAttemptManager.increaseAttempt(username);

            if (loginAttemptManager.isAttemptExhausted(username)) {
                //lock the user if the attempt is exhausted
                identityProvider.lock(username);
            }

            throw new LoginAttemptException(loginAttempt.getAttempts(),
                                            loginAttemptManager.getMaxAttempts(),
                                            loginAttemptManager.getAttemptTimeout());
        }

        throw new BadCredentialException();
    }

    @Override
    public void revoke(Authentication authentication) {
        // DO nothing
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(identityProvider, "identityProvider must be specified");
        Assert.notNull(tokenManager, "tokenManager must be specified");
        Assert.notNull(loginAttemptManager, "loginAttemptManager must be specified");
        Assert.notNull(featureConfigurer, "featureConfigurer must be specified");
    }

}

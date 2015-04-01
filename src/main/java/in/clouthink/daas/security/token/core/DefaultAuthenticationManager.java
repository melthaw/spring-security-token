package in.clouthink.daas.security.token.core;

import in.clouthink.daas.security.token.spi.AuthenticationProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class DefaultAuthenticationManager implements
                                         AuthenticationManager,
                                         InitializingBean {
    
    private List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();
    
    public List<AuthenticationProvider> getProviders() {
        return providers;
    }
    
    public void setProviders(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }
    
    public void addProvider(AuthenticationProvider provider) {
        this.providers.add(provider);
    }
    
    public void addProviders(AuthenticationProvider... providers) {
        for (AuthenticationProvider provider : providers) {
            this.providers.add(provider);
        }
    }
    
    @Override
    public Authentication login(AuthenticationRequest request) {
        if (request == null) {
            return null;
        }
        for (AuthenticationProvider provider : providers) {
            if (provider.supports(request)) {
                return provider.authenticate(request);
            }
        }
        return null;
    }
    
    @Override
    public void logout(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        for (AuthenticationProvider provider : providers) {
            provider.revoke(authentication);
        }
    }
    
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(providers, "providers must be specified");
    }
}

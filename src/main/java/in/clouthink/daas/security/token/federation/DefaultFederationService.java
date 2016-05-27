package in.clouthink.daas.security.token.federation;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.event.authentication.FederationLoginEvent;
import in.clouthink.daas.security.token.event.authentication.FederationLogoutEvent;
import in.clouthink.daas.security.token.spi.AuthenticationProvider;
import in.clouthink.daas.security.token.spi.FederationProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DefaultFederationService implements FederationService, ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private List<FederationProvider> federationProviders = new ArrayList<FederationProvider>();

	private List<AuthenticationProvider> authenticationProviders = new ArrayList<AuthenticationProvider>();

	public List<FederationProvider> getFederationProviders() {
		return federationProviders;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setFederationProviders(List<FederationProvider> federationProviders) {
		this.federationProviders = federationProviders;
	}

	public void addProvider(FederationProvider provider) {
		this.federationProviders.add(provider);
	}

	public void addProviders(FederationProvider... providers) {
		for (FederationProvider provider : providers) {
			this.federationProviders.add(provider);
		}
	}

	public List<AuthenticationProvider> getAuthenticationProviders() {
		return authenticationProviders;
	}

	public void setAuthenticationProviders(List<AuthenticationProvider> authenticationProviders) {
		this.authenticationProviders = authenticationProviders;
	}

	public void addProvider(AuthenticationProvider provider) {
		this.authenticationProviders.add(provider);
	}

	public void addProviders(AuthenticationProvider... providers) {
		for (AuthenticationProvider provider : providers) {
			this.authenticationProviders.add(provider);
		}
	}

	public Authentication login(FederationRequest request) {
		if (request == null) {
			return null;
		}
		for (FederationProvider provider : federationProviders) {
			if (provider.supports(request)) {
				try {
					Authentication result = provider.login(request);
					applicationContext.publishEvent(new FederationLoginEvent(request, result));
					return result;
				}
				catch (RuntimeException e) {
					applicationContext.publishEvent(new FederationLoginEvent(request, e));
					throw e;
				}
			}
		}
		return null;
	}

	@Override
	public void logout(Authentication authentication, Object logoutRequest) {
		if (authentication == null) {
			return;
		}
		for (AuthenticationProvider provider : authenticationProviders) {
			provider.revoke(authentication);
		}
		applicationContext.publishEvent(new FederationLogoutEvent(authentication, logoutRequest));
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(federationProviders, "federationProviders must be specified");
		Assert.notNull(authenticationProviders, "authenticationProviders must be specified");
	}

}

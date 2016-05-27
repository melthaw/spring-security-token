package in.clouthink.daas.security.token.event.authentication;

import in.clouthink.daas.security.token.core.Authentication;

/**
 * @since 1.5.0
 */
public class FederationLogoutEvent extends AbstractAuthenticationEvent {

	private Object logoutRequest;

	public FederationLogoutEvent(Authentication authentication, Object logoutRequest) {
		super(authentication);
		this.logoutRequest = logoutRequest;
	}

	@Override
	public Authentication getSource() {
		return (Authentication) super.getSource();
	}

	public Authentication getAuthentication() {
		return getSource();
	}

	public Object getLogoutRequest() {
		return logoutRequest;
	}

}

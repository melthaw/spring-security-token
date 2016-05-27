package in.clouthink.daas.security.token.event.authentication;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.federation.FederationRequest;

/**
 * @since 1.5.0
 */
public class FederationLoginEvent extends AbstractAuthenticationEvent {

	private Authentication authentication;

	private Exception exception;

	private boolean isAuthenticated;

	public FederationLoginEvent(FederationRequest federationRequest, Authentication authentication) {
		super(federationRequest);
		this.authentication = authentication;
		this.isAuthenticated = true;
	}

	public FederationLoginEvent(FederationRequest federationRequest, Exception exception) {
		super(federationRequest);
		this.exception = exception;
		this.isAuthenticated = false;
	}

	@Override
	public FederationRequest getSource() {
		return (FederationRequest) super.getSource();
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public Exception getException() {
		return exception;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}
}

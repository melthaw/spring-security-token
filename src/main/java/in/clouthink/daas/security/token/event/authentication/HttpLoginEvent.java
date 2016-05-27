package in.clouthink.daas.security.token.event.authentication;

import in.clouthink.daas.security.token.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 1.5.0
 */
public class HttpLoginEvent extends AbstractAuthenticationEvent {

	private Authentication authentication;

	private Exception exception;

	private boolean isAuthenticated;

	public HttpLoginEvent(HttpServletRequest httpRequest, Authentication authentication) {
		super(httpRequest);
		this.authentication = authentication;
		this.isAuthenticated = true;
	}

	public HttpLoginEvent(HttpServletRequest httpRequest, Exception exception) {
		super(httpRequest);
		this.exception = exception;
		this.isAuthenticated = false;
	}

	@Override
	public HttpServletRequest getSource() {
		return (HttpServletRequest) super.getSource();
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

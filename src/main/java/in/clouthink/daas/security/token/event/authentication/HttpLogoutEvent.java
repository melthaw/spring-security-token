package in.clouthink.daas.security.token.event.authentication;

import in.clouthink.daas.security.token.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 1.5.0
 */
public class HttpLogoutEvent extends AbstractAuthenticationEvent {

	private HttpServletRequest httpRequest;

	public HttpLogoutEvent(Authentication authentication, HttpServletRequest httpRequest) {
		super(authentication);
		this.httpRequest = httpRequest;
	}

	@Override
	public Authentication getSource() {
		return (Authentication) super.getSource();
	}

	public Authentication getAuthentication() {
		return getSource();
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

}

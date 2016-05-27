package in.clouthink.daas.security.token.federation;

import in.clouthink.daas.security.token.core.Authentication;

/**
 * The service to supply the login & logout for federation client
 */
public interface FederationService {

	/**
	 * @param request
	 * @return authentication if authenticated
	 */
	Authentication login(FederationRequest request);

	/**
	 * @param authentication
	 * @param logoutRequest  can be null if it is not required
	 */
	void logout(Authentication authentication, Object logoutRequest);

}

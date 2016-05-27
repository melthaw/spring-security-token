package in.clouthink.daas.security.token.federation;

/**
 * The implementation should provide the principal and something else required.
 */
public interface FederationRequest<T> {

	T getPrincipal();

}

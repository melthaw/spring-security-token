package in.clouthink.daas.security.token.exception;

/**
 * Multi-user-login not allowed, if one user is logged in , the same user logged in before will be offline (kick out) , if the user
 * continue to access the protected api , throw this exception.
 */
public class TokenDisabledException extends RuntimeException {

	public TokenDisabledException() {
	}

	public TokenDisabledException(String message) {
		super(message);
	}

	public TokenDisabledException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenDisabledException(Throwable cause) {
		super(cause);
	}
}

package in.clouthink.daas.security.token.exception;

/**
 */
public abstract class ErrorConstants {

    public static final String INVALID_USER_OR_PASSWORD = "error.invalidUserOrPassword";

    public static final String INVALID_TOKEN_OR_EXPIRED = "error.invalidTokenOrExpired";

    public static final String TOKEN_IS_DISABLED = "error.tokenIsDisabled";

    public static final String USER_IS_LOCKED = "error.userIsLocked";

    public static final String USER_IS_DISABLED = "error.userIsDisabled";

    public static final String USER_IS_EXPIRED = "error.userIsExpired";

    public static final String AUTHENTICATION_REQUIRED = "error.authenticationRequired";

    public static final String AUTHENTICATION_FAILED = "error.authenticationFailed";

    public static final String AUTHORIZATION_FAILED = "error.authorizationFailed";

    public static final String NO_PERMISSION = "error.noPermission";

    //@since 1.7.0
    public static final String LOGIN_ATTEMPT_FAILURE = "error.loginAttemptFailure";

    //@since 1.7.0
    public static final String LOGIN_LOCKED = "error.loginLocked";

}

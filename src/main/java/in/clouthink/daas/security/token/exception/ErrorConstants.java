package in.clouthink.daas.security.token.exception;

/**
 */
public abstract class ErrorConstants {

    public static final String USERNAME_REQUIRED = "error.usernameRequired";

    public static final String PASSWORD_REQUIRED = "error.passwordRequired";

    public static final String INVALID_USER_OR_PASSWORD = "error.invalidUserOrPassword";
    
    public static final String INVALID_TOKEN_OR_EXPIRED = "error.invalidTokenOrExpired";
    
    public static final String USER_IS_LOCKED = "error.userIsLocked";
    
    public static final String USER_IS_DISABLED = "error.userIsDisabled";
    
    public static final String USER_IS_EXPIRED = "error.userIsExpired";
    
    public static final String AUTHENTICATION_REQUIRED = "error.authenticationRequired";
    
    public static final String AUTHENTICATION_FAILED = "error.authenticationFailed";
    
    public static final String AUTHORIZATION_FAILED = "error.authorizationFailed";
    
    public static final String NO_PERMISSION = "error.noPermission";
    
}

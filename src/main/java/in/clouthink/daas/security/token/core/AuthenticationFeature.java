package in.clouthink.daas.security.token.core;

/**
 * @since 1.6.0
 */
public enum AuthenticationFeature implements ConfigFeature {

    /**
     * Feature that determines whether CORS is supported or not.
     * <p>
     * And false as default value.
     */
    CORS_SUPPORT(false),

    /**
     * Feature that determines whether token is required or not in http header .
     * <p>
     * And true as default value.
     */
    STRICT_TOKEN(true),

    /**
     * Feature that determines whether continue the AUTH filter chain if auth goes failure in <code>PreAuthenticationFilter</code>
     * <p>
     * And false as default value.
     */
    IGNORE_PRE_AUTHN_ERROR(false),

    /**
     * Feature that determines whether lock the user when user login failed and count to the max attempt.
     * <p>
     * And false as default value.
     *
     * @since 1.7.0
     */
    LOGIN_ATTEMPT_ENABLED(false),

    /**
     * Feature that determines whether user should pass the captcha verify before authentication.
     * <p>
     * And false as default value.
     *
     * @since 1.8.0
     */
    CAPTCHA_ENABLED(false),

    /**
     * Feature that determines whether post the login request only or not ( GET & PUT allowed if not)
     * <p>
     * And true as default value.
     *
     * @since 1.8.0
     */
    POST_LOGIN_ONLY(true);

    private final boolean _defaultState;
    private final int _mask;

    AuthenticationFeature(boolean defaultState) {
        _defaultState = defaultState;
        _mask = (1 << ordinal());
    }

    @Override
    public boolean enabledByDefault() {
        return _defaultState;
    }

    @Override
    public int getMask() {
        return _mask;
    }

}

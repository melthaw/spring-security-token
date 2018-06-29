package in.clouthink.daas.security.token.core;

/**
 * The configurer to enable & disable authn features.
 *
 * @since 1.6
 */
public class FeatureConfigurer {

    private AuthnConfig _authnConfig = new AuthnConfig();

    /**
     * Accessor for simple mapper features (which are shared for
     * authn, authz)
     */
    public final boolean isEnabled(AuthenticationFeature f) {
        return this._authnConfig.isEnabled(f);
    }

    /**
     * Accessor for simple mapper features (which are shared for
     * authn, authz)
     */
    public final boolean isDisabled(AuthenticationFeature f) {
        return !this._authnConfig.isEnabled(f);
    }

    /**
     * Method for changing state of an on/off authn feature for
     * this object mapper.
     */
    public final FeatureConfigurer configure(AuthenticationFeature f, boolean state) {
        this._authnConfig = state ? this._authnConfig.with(f) : this._authnConfig.without(f);
        return this;
    }

    /**
     * Method for enabling specified {@link AuthnConfig} features.
     * Modifies and returns this instance; no new object is created.
     */
    public final FeatureConfigurer enable(AuthenticationFeature feature) {
        this._authnConfig = this._authnConfig.with(feature);
        return this;
    }

    /**
     * Method for enabling specified {@link AuthnConfig} features.
     * Modifies and returns this instance; no new object is created.
     */
    public final FeatureConfigurer enable(AuthenticationFeature first,
                                          AuthenticationFeature... f) {
        this._authnConfig = this._authnConfig.with(first, f);
        return this;
    }

    /**
     * Method for enabling specified {@link AuthnConfig} features.
     * Modifies and returns this instance; no new object is created.
     */
    public final FeatureConfigurer disable(AuthenticationFeature feature) {
        this._authnConfig = this._authnConfig.without(feature);
        return this;
    }

    /**
     * Method for enabling specified {@link AuthnConfig} features.
     * Modifies and returns this instance; no new object is created.
     */
    public final FeatureConfigurer disable(AuthenticationFeature first,
                                           AuthenticationFeature... f) {
        this._authnConfig = this._authnConfig.without(first, f);
        return this;
    }

    static class AuthnConfig {

        /**
         * Method that calculates bit set (flags) of all features that
         * are enabled by default.
         */
        public static <F extends Enum<F> & ConfigFeature> int collectFeatureDefaults(
                Class<F> enumClass) {
            int flags = 0;
            for (F value : enumClass.getEnumConstants()) {
                if (value.enabledByDefault()) {
                    flags |= value.getMask();
                }
            }
            return flags;
        }

        /**
         * Set of {@link AuthenticationFeature}s enabled.
         */
        protected final int _authnFeatures;

        public AuthnConfig() {
            this._authnFeatures = collectFeatureDefaults(AuthenticationFeature.class);
        }

        public AuthnConfig(int authnFeatures) {
            this._authnFeatures = authnFeatures;
        }

        public final boolean isEnabled(AuthenticationFeature f) {
            return (this._authnFeatures & f.getMask()) != 0;
        }

        /**
         * Fluent factory method that will construct and return a new configuration
         * object instance with specified features enabled.
         */
        public AuthnConfig with(AuthenticationFeature feature) {
            int newAuthnFeatures = (this._authnFeatures | feature.getMask());
            return (newAuthnFeatures == this._authnFeatures) ? this :
                    new AuthnConfig(newAuthnFeatures);
        }

        /**
         * Fluent factory method that will construct and return a new configuration
         * object instance with specified features enabled.
         */
        public AuthnConfig with(AuthenticationFeature first,
                                AuthenticationFeature... features) {
            int newAuthnFeatures = _authnFeatures | first.getMask();
            for (AuthenticationFeature f : features) {
                newAuthnFeatures |= f.getMask();
            }
            return (newAuthnFeatures == _authnFeatures) ? this :
                    new AuthnConfig(newAuthnFeatures);
        }

        /**
         * Fluent factory method that will construct and return a new configuration
         * object instance with specified features enabled.
         */
        public AuthnConfig withFeatures(AuthenticationFeature... features) {
            int newAuthnFeatures = _authnFeatures;
            for (AuthenticationFeature f : features) {
                newAuthnFeatures |= f.getMask();
            }
            return (newAuthnFeatures == _authnFeatures) ? this :
                    new AuthnConfig(newAuthnFeatures);
        }

        /**
         * Fluent factory method that will construct and return a new configuration
         * object instance with specified feature disabled.
         */
        public AuthnConfig without(AuthenticationFeature feature) {
            int newAuthnFeatures = _authnFeatures & ~feature.getMask();
            return (newAuthnFeatures == _authnFeatures) ? this :
                    new AuthnConfig(newAuthnFeatures);
        }

        /**
         * Fluent factory method that will construct and return a new configuration
         * object instance with specified features disabled.
         */
        public AuthnConfig without(AuthenticationFeature first,
                                   AuthenticationFeature... features) {
            int newAuthnFeatures = _authnFeatures & ~first.getMask();
            for (AuthenticationFeature f : features) {
                newAuthnFeatures &= ~f.getMask();
            }
            return (newAuthnFeatures == _authnFeatures) ? this :
                    new AuthnConfig(newAuthnFeatures);
        }

        /**
         * Fluent factory method that will construct and return a new configuration
         * object instance with specified features disabled.
         */
        public AuthnConfig withoutFeatures(AuthenticationFeature... features) {
            int newAuthnFeatures = _authnFeatures;
            for (AuthenticationFeature f : features) {
                newAuthnFeatures &= ~f.getMask();
            }
            return (newAuthnFeatures == _authnFeatures) ? this :
                    new AuthnConfig(newAuthnFeatures);
        }

    }

}

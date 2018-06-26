package in.clouthink.daas.security.token.core;


/**
 * The interface is taken from <a href="https://github.com/FasterXML/jackson-core">FasterXML Jackson</a>
 *
 * @since 1.6
 */
public interface ConfigFeature {

    /**
     * Accessor for checking whether this feature is enabled by default.
     */
    boolean enabledByDefault();

    /**
     * Returns bit mask for this feature instance
     */
    int getMask();

}

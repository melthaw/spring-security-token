package in.clouthink.daas.security.token.spi;

/**
 * KeyGenerator
 */
public interface KeyGenerator {
    
    /**
     * @return random id (hex format)
     */
    public String generateId();
    
    /**
     * @return random key (hex format)
     */
    public String generateHexKey();
    
    /**
     * @return random key (base32 format)
     */
    public String generateBase32Key();
    
    /**
     * @return random key (base64 format)
     */
    public String generateBase64Key();
    
}

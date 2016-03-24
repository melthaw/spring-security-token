package in.clouthink.daas.security.token.spi;

/**
 * KeyGenerator
 */
public interface KeyGenerator {
    
    /**
     * @return random id (hex format)
     */
    String generateId();
    
    /**
     * @return random key (hex format)
     */
    String generateHexKey();
    
    /**
     * @return random key (base32 format)
     */
    String generateBase32Key();
    
    /**
     * @return random key (base64 format)
     */
    String generateBase64Key();
    
}

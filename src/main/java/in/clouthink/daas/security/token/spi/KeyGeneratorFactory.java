package in.clouthink.daas.security.token.spi;

import java.util.ServiceLoader;

/**
 */
public class KeyGeneratorFactory {
    
    private static class KeyGeneratorHolder {
        
        static KeyGenerator instance;
        
        static {
            ServiceLoader<KeyGenerator> serviceLoader = ServiceLoader.load(KeyGenerator.class);
            if (serviceLoader != null) {
                instance = serviceLoader.iterator().next();
            }
        }
        
    }
    
    public static KeyGenerator getInstance() {
        return KeyGeneratorHolder.instance;
    }
    
    private KeyGeneratorFactory() {
        
    }
}

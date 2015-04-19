package in.clouthink.daas.security.token.spi;

import java.util.ServiceLoader;

public class PasswordDigesterProviderFactory {
    
    static class PasswordDigesterProviderHolder {
        
        static PasswordDigesterProvider instance;
        
        static {
            ServiceLoader<PasswordDigesterProvider> servantLoader = ServiceLoader.load(PasswordDigesterProvider.class);
            if (servantLoader != null) {
                instance = servantLoader.iterator().next();
            }
        }
        
    }
    
    public static PasswordDigesterProvider getInstance() {
        return PasswordDigesterProviderHolder.instance;
    }
    
    private PasswordDigesterProviderFactory() {
        
    }
}

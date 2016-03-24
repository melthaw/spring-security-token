package in.clouthink.daas.security.token.spi;

import in.clouthink.daas.security.token.core.User;

/**
 */
public interface DigestMetadataProvider<T extends User> {
    
    public static final DigestMetadataProvider INSTANCE = new DigestMetadataProvider<User>() {
        @Override
        public String getDigestAlgorithm(User user) {
            return "MD5";
        }
        
        @Override
        public String getSalt(User user) {
            return "";
        }
    };
    
    String getDigestAlgorithm(T user);
    
    String getSalt(T user);
    
}

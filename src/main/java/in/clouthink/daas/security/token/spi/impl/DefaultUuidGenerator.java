package in.clouthink.daas.security.token.spi.impl;

import in.clouthink.daas.security.token.repackage.org.apache.commons.codec.binary.Base32;
import in.clouthink.daas.security.token.repackage.org.apache.commons.codec.binary.Base64;
import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Hex;
import in.clouthink.daas.security.token.spi.KeyGenerator;

import java.util.UUID;

/**
 */
public class DefaultUuidGenerator implements KeyGenerator {
    
    @Override
    public String generateId() {
        return generateHexKey();
    }
    
    @Override
    public String generateHexKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    @Override
    public String generateBase32Key() {
        byte[] hexBytes = Hex.decode(UUID.randomUUID()
                                         .toString()
                                         .replace("-", ""));
        return new Base32().encodeAsString(hexBytes);
    }
    
    @Override
    public String generateBase64Key() {
        byte[] hexBytes = Hex.decode(UUID.randomUUID()
                                         .toString()
                                         .replace("-", ""));
        return new Base64().encodeAsString(hexBytes);
    }
    
}

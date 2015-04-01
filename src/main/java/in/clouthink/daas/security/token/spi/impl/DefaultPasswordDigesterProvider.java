package in.clouthink.daas.security.token.spi.impl;

import in.clouthink.daas.security.token.core.pwd.PasswordDigester;
import in.clouthink.daas.security.token.spi.PasswordDigesterProvider;
import in.clouthink.daas.security.token.core.pwd.DefaultPasswordDigester;
import in.clouthink.daas.security.token.core.pwd.DigestAlgorithm;
import in.clouthink.daas.security.token.exception.UnsupportedDigestAlgorithmException;

/**
 */
public class DefaultPasswordDigesterProvider implements
                                            PasswordDigesterProvider {
    
    private PasswordDigester md5digester;
    
    private PasswordDigester sha1digester;
    
    private PasswordDigester sha256digester;
    
    public DefaultPasswordDigesterProvider() {
        md5digester = new DefaultPasswordDigester(DigestAlgorithm.MD5);
        sha1digester = new DefaultPasswordDigester(DigestAlgorithm.SHA1);
        sha256digester = new DefaultPasswordDigester(DigestAlgorithm.SHA256);
    }
    
    @Override
    public PasswordDigester getPasswordDigester(String digestAlgorithm) {
        // TODO merge all
        if (DigestAlgorithm.MD5.equalsIgnoreCase(digestAlgorithm)) {
            return md5digester;
        }
        else if (DigestAlgorithm.SHA1.equalsIgnoreCase(digestAlgorithm)) {
            return sha1digester;
        }
        else if (DigestAlgorithm.SHA256.equalsIgnoreCase(digestAlgorithm)) {
            return sha256digester;
        }
        
        throw new UnsupportedDigestAlgorithmException(digestAlgorithm);
    }
}

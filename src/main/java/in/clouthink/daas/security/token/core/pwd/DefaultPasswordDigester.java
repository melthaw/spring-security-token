package in.clouthink.daas.security.token.core.pwd;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import in.clouthink.daas.security.token.exception.UnsupportedDigestAlgorithmException;
import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Hex;

/**
 */
public class DefaultPasswordDigester implements PasswordDigester {
    
    private String digestAlgorithm;
    
    private boolean base64Encoded = false;
    
    public DefaultPasswordDigester(String digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }
    
    public boolean isBase64Encoded() {
        return base64Encoded;
    }
    
    public void setBase64Encoded(boolean base64Encoded) {
        this.base64Encoded = base64Encoded;
    }
    
    public MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(digestAlgorithm);
        }
        catch (NoSuchAlgorithmException e) {
            throw new UnsupportedDigestAlgorithmException(e);
        }
    }
    
    @Override
    public String encode(String rawPassword, String salt) {
        String saltedPass = mergePasswordAndSalt(rawPassword, salt, false);
        byte[] passBytes = utf8bytes(saltedPass);
        
        MessageDigest messageDigest = getMessageDigest();
        messageDigest.update(passBytes, 0, passBytes.length);
        
        byte[] resBuf = messageDigest.digest();
        
        if (isBase64Encoded()) {
            return utf8string(Base64.encode(resBuf));
        }
        else {
            return new String(Hex.encode(resBuf));
        }
    }
    
    @Override
    public boolean matches(String rawPassword,
                           String encodedPassword,
                           String salt) {
        String pass1 = "" + encodedPassword;
        String pass2 = encode(rawPassword, salt);
        try {
            return equals(pass1, pass2);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected String mergePasswordAndSalt(String password,
                                          String salt,
                                          boolean strict) {
        if (password == null) {
            password = "";
        }
        
        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString()
                                                                 .lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }
        
        if ((salt == null) || "".equals(salt)) {
            return password;
        }
        else {
            return password + "{" + salt.toString() + "}";
        }
    }
    
    static boolean equals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        int expectedLength = expectedBytes == null ? -1 : expectedBytes.length;
        int actualLength = actualBytes == null ? -1 : actualBytes.length;
        if (expectedLength != actualLength) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < expectedLength; i++) {
            result |= expectedBytes[i] ^ actualBytes[i];
        }
        return result == 0;
    }
    
    private static byte[] bytesUtf8(String s) {
        if (s == null) {
            return null;
        }
        
        return utf8bytes(s);
    }
    
    private static byte[] utf8bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String utf8string(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

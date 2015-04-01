package in.clouthink.daas.security.token.exception;

/**
 */
public class UnsupportedDigestAlgorithmException extends RuntimeException {
    
    public UnsupportedDigestAlgorithmException() {
    }
    
    public UnsupportedDigestAlgorithmException(String message) {
        super(message);
    }
    
    public UnsupportedDigestAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UnsupportedDigestAlgorithmException(Throwable cause) {
        super(cause);
    }
}

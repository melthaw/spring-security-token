package in.clouthink.daas.security.token.core;

import java.io.Serializable;

public interface Authentication extends Serializable {
    
    public Token currentToken();
    
}

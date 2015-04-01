package in.clouthink.daas.security.token.spi;

import java.util.List;

import in.clouthink.daas.security.token.core.acl.Acl;

public interface AclProvider<T extends Acl> {
    
    public List<T> listAll();
    
}

package in.clouthink.daas.security.token.spi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.clouthink.daas.security.token.core.acl.UrlAcl;
import in.clouthink.daas.security.token.spi.AclProvider;

/**
 */
public class DefaultUrlAclProvider implements AclProvider<UrlAcl> {
    
    private List<UrlAcl> acls = new ArrayList<UrlAcl>();
    
    public DefaultUrlAclProvider(List<UrlAcl> acls) {
        this.acls.addAll(acls);
    }
    
    @Override
    public List<UrlAcl> listAll() {
        return Collections.unmodifiableList(acls);
    }
    
}

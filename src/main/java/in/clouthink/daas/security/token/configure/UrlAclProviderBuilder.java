package in.clouthink.daas.security.token.configure;

import java.util.ArrayList;
import java.util.List;

import in.clouthink.daas.security.token.core.acl.UrlAcl;
import in.clouthink.daas.security.token.spi.AclProvider;
import in.clouthink.daas.security.token.spi.impl.DefaultUrlAclProvider;

/**
 */
public class UrlAclProviderBuilder {
    
    public static UrlAclProviderBuilder newInstance() {
        return new UrlAclProviderBuilder();
    }
    
    private List<UrlAcl> urlAcls = new ArrayList<UrlAcl>();
    
    public UrlAclProviderBuilder add(UrlAclBuilder urlAclBuilder) {
        urlAcls.add(urlAclBuilder.build());
        return this;
    }
    
    public AclProvider build() {
        return new DefaultUrlAclProvider(urlAcls);
    }
    
}

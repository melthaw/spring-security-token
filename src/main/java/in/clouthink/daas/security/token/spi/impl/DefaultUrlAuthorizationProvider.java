package in.clouthink.daas.security.token.spi.impl;

import in.clouthink.daas.security.token.core.acl.*;
import in.clouthink.daas.security.token.spi.AclProvider;
import in.clouthink.daas.security.token.spi.AuthorizationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class DefaultUrlAuthorizationProvider implements
                                            AuthorizationProvider<UrlAccessRequest>,
                                            InitializingBean {
    
    private static final Log logger = LogFactory.getLog(DefaultUrlAuthorizationProvider.class);
    
    private AclProvider<UrlAcl> provider;
    
    private List<AccessRequestVoter<UrlAccessRequest>> voters = new ArrayList<AccessRequestVoter<UrlAccessRequest>>();
    
    public AclProvider<UrlAcl> getProvider() {
        return provider;
    }
    
    public void setProvider(AclProvider<UrlAcl> provider) {
        this.provider = provider;
    }
    
    public List<AccessRequestVoter<UrlAccessRequest>> getVoters() {
        return voters;
    }
    
    public void setVoters(List<AccessRequestVoter<UrlAccessRequest>> voters) {
        this.voters = voters;
    }
    
    public void addVoter(AccessRequestVoter<UrlAccessRequest> voter) {
        this.voters.add(voter);
    }
    
    @Override
    public AccessResponse authorize(UrlAccessRequest accessRequest) {
        AccessResponse result = doAuthorize(accessRequest);
        logger.debug("Authorization result:" + result);
        return result;
    }
    
    private AccessResponse doAuthorize(UrlAccessRequest accessRequest) {
        for (UrlAcl urlAcl : provider.listAll()) {
            if (urlAcl.matches(accessRequest.getTarget(),
                               accessRequest.getOperation())) {
                for (String grantRule : urlAcl.getGrantRules()) {
                    for (AccessRequestVoter<UrlAccessRequest> voter : voters) {
                        AccessResponse accessResponse = voter.vote(accessRequest,
                                                                   grantRule);
                        if (AccessResponse.APPROVED == accessResponse) {
                            return accessResponse;
                        }
                    }
                }
                return AccessResponse.REFUESD;
            }
        }
        return AccessResponse.ABANDON;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(provider);
        Assert.notEmpty(voters);
    }
}

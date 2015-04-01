package in.clouthink.daas.security.token.configure;

import in.clouthink.daas.security.token.core.acl.AntPathUrlAcl;
import in.clouthink.daas.security.token.core.acl.HttpMethod;
import in.clouthink.daas.security.token.core.acl.RegexUrlAcl;
import in.clouthink.daas.security.token.core.acl.UrlAcl;
import org.springframework.util.StringUtils;

/**
 */
public class UrlAclBuilder {
    
    enum UrlAclType {
        REGEX, ANTPATH;
    }
    
    public static UrlAclBuilder regexBuilder() {
        return new UrlAclBuilder(UrlAclType.REGEX);
    }
    
    public static UrlAclBuilder antPathBuilder() {
        return new UrlAclBuilder(UrlAclType.ANTPATH);
    }
    
    UrlAclType type;
    
    String urlRealm;
    
    String[] httpMethods = new String[] { HttpMethod.ALL };
    
    String[] grantRules = new String[] {};
    
    private boolean caseSensitive;
    
    UrlAclBuilder(UrlAclType type) {
        this.type = type;
    }
    
    public UrlAclBuilder url(String url) {
        this.urlRealm = url;
        return this;
    }
    
    public UrlAclBuilder caseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }
    
    public UrlAclBuilder httpMethods(String... httpMethods) {
        this.httpMethods = httpMethods;
        return this;
    }
    
    public UrlAclBuilder grantRules(String... grantRules) {
        String[] parsedRules = new String[] {};
        for (String grantRule : grantRules) {
            if (grantRule.indexOf(",") > 0) {
                parsedRules = StringUtils.concatenateStringArrays(parsedRules,
                                                                  grantRule.split(","));
            }
            else {
                parsedRules = StringUtils.concatenateStringArrays(parsedRules,
                                                                  new String[] { grantRule });
            }
        }
        this.grantRules = parsedRules;
        return this;
    }
    
    public UrlAcl build() {
        // TODO add validation
        if (UrlAclType.ANTPATH == type) {
            return new RegexUrlAcl(this.urlRealm,
                                   this.httpMethods,
                                   this.caseSensitive,
                                   this.grantRules);
        }
        else if (UrlAclType.REGEX == type) {
            return new AntPathUrlAcl(this.urlRealm,
                                     this.httpMethods,
                                     this.caseSensitive,
                                     this.grantRules);
        }
        return null;
    }
}

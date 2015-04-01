package in.clouthink.daas.security.token.core.acl;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 */
public class RegexUrlMatcher implements UrlMatcher {
    
    private final static Log logger = LogFactory.getLog(RegexUrlMatcher.class);
    
    private final Pattern pattern;
    
    private final String[] httpMethods;
    
    public RegexUrlMatcher(String pattern) {
        this(pattern, HttpMethod.ALL, false);
    }
    
    public RegexUrlMatcher(String pattern, String... httpMethods) {
        this(pattern, httpMethods, false);
    }
    
    public RegexUrlMatcher(String pattern,
                           String httpMethod,
                           boolean caseInsensitive) {
        this(pattern, new String[] { httpMethod }, caseInsensitive);
    }
    
    public RegexUrlMatcher(String pattern,
                           String[] httpMethods,
                           boolean caseInsensitive) {
        if (caseInsensitive) {
            this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        }
        else {
            this.pattern = Pattern.compile(pattern);
        }
        
        this.httpMethods = (httpMethods == null || httpMethods.length == 0) ? new String[] { HttpMethod.ALL }
                                                                           : httpMethods;
    }
    
    /**
     * Performs the match of the request URL (
     * {@code servletPath + pathInfo + queryString}) against the compiled
     * pattern. If the query string is present, a question mark will be
     * prepended
     * 
     * @param httpMethod
     * @param url
     * @return true if the pattern matched
     */
    @Override
    public boolean matches(String url, String httpMethod) {
        if (httpMethod != null) {
            boolean httpMethodMatched = false;
            for (String hm : httpMethods) {
                if (HttpMethod.ALL.equalsIgnoreCase(hm)) {
                    httpMethodMatched = true;
                    break;
                }
                if (hm.equalsIgnoreCase(httpMethod)) {
                    httpMethodMatched = true;
                    break;
                }
            }
            if (!httpMethodMatched) {
                return false;
            }
        }
        
        return pattern.matcher(url).matches();
    }
    
}

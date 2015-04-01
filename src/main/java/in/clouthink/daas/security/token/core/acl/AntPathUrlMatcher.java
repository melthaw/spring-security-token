package in.clouthink.daas.security.token.core.acl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;

/**
 */
public class AntPathUrlMatcher implements UrlMatcher {
    
    private final static Log logger = LogFactory.getLog(AntPathUrlMatcher.class);
    
    private static final String MATCH_ALL = "/**";
    
    private String pattern;
    
    private Matcher matcher;
    
    private String[] httpMethods;
    
    private boolean caseSensitive;
    
    public AntPathUrlMatcher(String pattern) {
        this(pattern, HttpMethod.ALL, false);
    }
    
    public AntPathUrlMatcher(String pattern, String... httpMethods) {
        this(pattern, httpMethods, false);
    }
    
    public AntPathUrlMatcher(String pattern,
                             String httpMethod,
                             boolean caseInsensitive) {
        this(pattern, new String[] { httpMethod }, caseInsensitive);
    }
    
    public AntPathUrlMatcher(String pattern,
                             String[] httpMethods,
                             boolean caseInsensitive) {
        if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
            pattern = MATCH_ALL;
            matcher = null;
        }
        else {
            if (!caseSensitive) {
                pattern = pattern.toLowerCase();
            }
            
            // If the pattern ends with {@code /**} and has no other wildcards,
            // then optimize to a sub-path match
            if (pattern.endsWith(MATCH_ALL) && pattern.indexOf('?') == -1
                && pattern.indexOf("*") == pattern.length() - 2) {
                matcher = new SubpathMatcher(pattern.substring(0,
                                                               pattern.length() - 3));
            }
            else {
                matcher = new SpringAntMatcher(pattern);
            }
        }
        
        this.pattern = pattern;
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
        
        if (pattern.equals(MATCH_ALL)) {
            return true;
        }
        
        if (!caseSensitive) {
            return matcher.matches(url.toLowerCase());
        }
        
        return matcher.matches(url);
    }
    
    private static interface Matcher {
        boolean matches(String path);
    }
    
    private static class SpringAntMatcher implements Matcher {
        private static final AntPathMatcher antMatcher = new AntPathMatcher();
        
        private final String pattern;
        
        private SpringAntMatcher(String pattern) {
            this.pattern = pattern;
        }
        
        public boolean matches(String path) {
            return antMatcher.match(pattern, path);
        }
    }
    
    /**
     * Optimized matcher for trailing wildcards
     */
    private static class SubpathMatcher implements Matcher {
        private final String subpath;
        
        private final int length;
        
        private SubpathMatcher(String subpath) {
            assert !subpath.contains("*");
            this.subpath = subpath;
            this.length = subpath.length();
        }
        
        public boolean matches(String path) {
            return path.startsWith(subpath) && (path.length() == length || path.charAt(length) == '/');
        }
    }
}

package in.clouthink.daas.security.token.support.i18n;

import java.util.Locale;

/**
 */
public interface MessageProvider {
    
    public void setLocale(Locale locale);
    
    public String getMessage(String code);
    
}

package in.clouthink.daas.security.token.support.i18n;

import java.util.Locale;

/**
 * The i18n message provider
 */
public interface MessageProvider {

    void setLocale(Locale locale);

    String getMessage(String code);

}

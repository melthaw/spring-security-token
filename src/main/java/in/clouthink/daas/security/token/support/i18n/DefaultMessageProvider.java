package in.clouthink.daas.security.token.support.i18n;

import in.clouthink.daas.security.token.exception.ErrorConstants;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 */
public class DefaultMessageProvider implements MessageProvider {

    private Locale locale = Locale.ENGLISH;

    private ResourceBundle rbEn = ResourceBundle.getBundle("daas_token_message",
                                                           Locale.ENGLISH);

    private ResourceBundle rbZh = ResourceBundle.getBundle("daas_token_message",
                                                           Locale.CHINESE);

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String getMessage(String code) {
        if (locale == Locale.CHINESE || locale == Locale.CHINA
                || locale == Locale.SIMPLIFIED_CHINESE
                || locale == Locale.TRADITIONAL_CHINESE) {
            return rbZh.getString(code);
        }
        else {
            return rbEn.getString(code);
        }
    }

//    public static void main(String[] args) {
//        DefaultMessageProvider messageProvider = new DefaultMessageProvider();
//        messageProvider.setLocale(Locale.CHINESE);
//        System.out.print(messageProvider.getMessage(ErrorConstants.AUTHENTICATION_REQUIRED));
//    }

}

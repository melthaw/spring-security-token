package in.clouthink.daas.security.token.spi.impl.model;

import in.clouthink.daas.security.token.core.Captcha;
import in.clouthink.daas.security.token.core.LoginAttempt;
import in.clouthink.daas.security.token.spi.KeyGeneratorFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * CaptchaEntity
 *
 * @since 1.8.0
 */
@Document(collection = "Captcha")
public class CaptchaEntity implements Captcha {

    public static CaptchaEntity create(String value, long timeout) {
        CaptchaEntity result = new CaptchaEntity();
        String id = KeyGeneratorFactory.getInstance().generateId();
        result.setId(id);
        result.setValue(value);
        result.setExpiredTime(new Date(System.currentTimeMillis() + timeout));
        return result;
    }

    @Id
    private String id;

    private String value;

    private Date expiredTime;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public String toString() {
        return "CaptchaEntity{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", expiredTime=" + expiredTime +
                '}';
    }

}

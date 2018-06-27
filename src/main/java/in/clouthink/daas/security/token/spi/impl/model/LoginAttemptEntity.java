package in.clouthink.daas.security.token.spi.impl.model;

import in.clouthink.daas.security.token.core.LoginAttempt;
import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;
import in.clouthink.daas.security.token.spi.KeyGeneratorFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @since 1.7.0
 */
@Document(collection = "LoginAttempts")
public class LoginAttemptEntity implements LoginAttempt {

    public static LoginAttemptEntity create(String username, long timeout) {
        LoginAttemptEntity result = new LoginAttemptEntity();
        result.setId(username);
        result.setUsername(username);
        result.setAttempts((short) 0);
        result.setExpiredTime(new Date(System.currentTimeMillis() + timeout));
        result.setLatestTime(new Date());
        return result;
    }

    @Id
    private String id;

    @Indexed
    private String username;

    private short attempts = 0;

    private Date expiredTime;

    private Date latestTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public short getAttempts() {
        return attempts;
    }

    public void setAttempts(short attempts) {
        this.attempts = attempts;
    }

    @Override
    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }

    @Override
    public void increaseAttempt() {
        this.attempts += 1;
        this.latestTime = new Date();
    }

    @Override
    public String toString() {
        return "LoginAttemptEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", attempts=" + attempts +
                ", expiredTime=" + expiredTime +
                ", latestTime=" + latestTime +
                '}';
    }

}

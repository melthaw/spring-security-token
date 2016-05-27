package in.clouthink.daas.security.token.spi.impl.model;

import java.util.Date;
import java.util.UUID;

import in.clouthink.daas.security.token.spi.KeyGeneratorFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;

@Document(collection = "Tokens")
public class TokenEntity implements Token {
    
    public static TokenEntity create(User owner) {
        TokenEntity result = new TokenEntity();
        String value = KeyGeneratorFactory.getInstance().generateId();
        result.setToken(value);
        result.setOwner(owner);
        Date date = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        result.setExpiredDate(date);
        result.setLatestTime(new Date());
        return result;
    }
    
    public static TokenEntity create(User owner, long timeout) {
        TokenEntity result = new TokenEntity();
        String value = KeyGeneratorFactory.getInstance().generateId();
        result.setToken(value);
        result.setOwner(owner);
        Date date = new Date(System.currentTimeMillis() + timeout);
        result.setExpiredDate(date);
        result.setLatestTime(new Date());
        return result;
    }
    
    @Id
    private String token;
    
    @Indexed
    @DBRef
    private User owner;
    
    private Date expiredDate;
    
    private Date latestTime;
    
    private boolean enabled = true;
    
    @Override
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    @Override
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    @Override
    public Date getExpiredDate() {
        return expiredDate;
    }
    
    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }
    
    @Override
    public Date getLatestTime() {
        return latestTime;
    }
    
    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void updateExpiredDate(long timeout) {
        Date date = new Date(System.currentTimeMillis() + timeout);
        this.setExpiredDate(date);
        this.setLatestTime(new Date());
    }
    
    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public String toString() {
        return "TokenEntity{" +
               "token='" + token + '\'' +
               ", owner=" + owner +
               ", expiredDate=" + expiredDate +
               ", latestTime=" + latestTime +
               ", enabled=" + enabled +
               '}';
    }
}

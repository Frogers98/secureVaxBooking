package app.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "incorrect_login")
public class IncorrectLogin {
    private String ipAddress;
    private int numAttempts;
    private boolean ipNonLocked = true;
    @Column(name = "lock_time")
    private Date lockTime;

    @Id
    @GeneratedValue
    private Long incorrect_login_id;

    public IncorrectLogin(String ipAddress) {
        // Whenever a new incorrect login is attempted record the ipAddress and instantiate the counter of incorrect
        // attempts at 1
        this.ipAddress = ipAddress;
        this.numAttempts = 1;
    }

    public IncorrectLogin() {

    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getNumAttempts() {
        return this.numAttempts;
    }

    public void setNumAttempts(int numAttempts) {
        this.numAttempts = numAttempts;
    }

    public boolean isIpNonLocked() {
        return ipNonLocked;
    }

    public void setIpNonLocked(boolean ipNonLocked) {
        this.ipNonLocked = ipNonLocked;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }
}
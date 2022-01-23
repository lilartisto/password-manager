package pl.edu.pw.manager.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String masterPassword;
    private Boolean accountNonLocked = true;
    private Integer failedAttempt = 0;
    private Date lockTime;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ServicePassword> passwords;

    public User() {
    }

    public User(Long id, String username, String password, String masterPassword, Boolean accountNonLocked,
                Integer failedAttempt, Date lockTime, List<ServicePassword> passwords) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.masterPassword = masterPassword;
        this.accountNonLocked = accountNonLocked;
        this.failedAttempt = failedAttempt;
        this.lockTime = lockTime;
        this.passwords = passwords;
    }

    public User(String username, String password, String masterPassword) {
        this.username = username;
        this.password = password;
        this.masterPassword = masterPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public List<ServicePassword> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<ServicePassword> passwords) {
        this.passwords = passwords;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Integer getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(Integer failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }
}

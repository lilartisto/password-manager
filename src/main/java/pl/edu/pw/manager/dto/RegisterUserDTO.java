package pl.edu.pw.manager.dto;

public class RegisterUserDTO {

    private String username;
    private String password;
    private String passwordRepeat;
    private String masterPassword;
    private String masterPasswordRepeat;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(String username, String password, String passwordRepeat, String masterPassword, String masterPasswordRepeat) {
        this.username = username;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.masterPassword = masterPassword;
        this.masterPasswordRepeat = masterPasswordRepeat;
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

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public String getMasterPasswordRepeat() {
        return masterPasswordRepeat;
    }

    public void setMasterPasswordRepeat(String masterPasswordRepeat) {
        this.masterPasswordRepeat = masterPasswordRepeat;
    }
}


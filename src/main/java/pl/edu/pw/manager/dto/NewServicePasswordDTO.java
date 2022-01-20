package pl.edu.pw.manager.dto;

public class NewServicePasswordDTO {

    private String serviceName;
    private String password;
    private String passwordRepeat;
    private String masterPassword;

    public NewServicePasswordDTO() {
    }

    public NewServicePasswordDTO(String serviceName, String password, String passwordRepeat, String masterPassword) {
        this.serviceName = serviceName;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.masterPassword = masterPassword;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
}

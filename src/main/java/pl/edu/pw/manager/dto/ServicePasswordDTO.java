package pl.edu.pw.manager.dto;

public class ServicePasswordDTO {

    private Long id;
    private String serviceName;
    private String password;

    public ServicePasswordDTO() {
    }

    public ServicePasswordDTO(Long id, String serviceName, String password) {
        this.id = id;
        this.serviceName = serviceName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

package pl.edu.pw.manager.domain;

import javax.persistence.*;

@Entity
@Table
public class ServicePassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceName;
    private String password;

    public ServicePassword() {
    }

    public ServicePassword(Long id, String name, String password) {
        this.id = id;
        this.serviceName = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return serviceName;
    }

    public void setName(String name) {
        this.serviceName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

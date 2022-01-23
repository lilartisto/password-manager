package pl.edu.pw.manager.service.mapper;

import pl.edu.pw.manager.domain.ServicePassword;
import pl.edu.pw.manager.dto.ServicePasswordDTO;

import java.util.LinkedList;
import java.util.List;

public class ServicePasswordMapper {

    public List<ServicePasswordDTO> map(List<ServicePassword> passwords) {
        List<ServicePasswordDTO> servicePasswords = new LinkedList<>();

        if (passwords != null) {
            for (ServicePassword password : passwords) {
                servicePasswords.add(new ServicePasswordDTO(
                        password.getId(),
                        password.getServiceName(),
                        null
                ));
            }
        }

        return servicePasswords;
    }

}

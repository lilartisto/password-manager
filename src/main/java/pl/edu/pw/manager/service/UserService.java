package pl.edu.pw.manager.service;

import pl.edu.pw.manager.dto.NewServicePasswordDTO;
import pl.edu.pw.manager.dto.RegisterUserDTO;
import pl.edu.pw.manager.dto.ServicePasswordDTO;

import java.util.List;

public interface UserService {

    void registerUser(RegisterUserDTO user);
    void addNewPassword(String username, NewServicePasswordDTO newPassword) throws Exception;
    List<ServicePasswordDTO> getServicePasswords(String username);
    ServicePasswordDTO getServicePassword(String username, Long servicePasswordId, String masterPassword) throws Exception;

}

package pl.edu.pw.manager.service;

import pl.edu.pw.manager.domain.User;
import pl.edu.pw.manager.dto.NewServicePasswordDTO;
import pl.edu.pw.manager.dto.RegisterUserDTO;
import pl.edu.pw.manager.dto.ServicePasswordDTO;

import java.util.List;

public interface UserService {

    int MAX_FAILED_ATTEMPTS = 5;
    int LOCK_TIME_DURATION = 60 * 1000;

    User getByUsername(String username);

    void registerUser(RegisterUserDTO user);

    void addNewPassword(String username, NewServicePasswordDTO newPassword) throws Exception;

    List<ServicePasswordDTO> getServicePasswords(String username);

    ServicePasswordDTO getServicePassword(String username, Long servicePasswordId, String masterPassword) throws Exception;

    void deletePassword(String username, Long servicePasswordId, String masterPassword);

    void increaseFailedAttempts(User user);

    void lock(User user);

    boolean unlockWhenTimeExpired(User user);

    void resetFailedAttempts(User user);

}

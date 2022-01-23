package pl.edu.pw.manager.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.manager.domain.ServicePassword;
import pl.edu.pw.manager.domain.User;
import pl.edu.pw.manager.dto.NewServicePasswordDTO;
import pl.edu.pw.manager.dto.RegisterUserDTO;
import pl.edu.pw.manager.dto.ServicePasswordDTO;
import pl.edu.pw.manager.repository.UserRepository;
import pl.edu.pw.manager.security.cipher.AESAdapter;
import pl.edu.pw.manager.security.validation.NewServicePasswordValidator;
import pl.edu.pw.manager.security.validation.RegisterUserValidator;
import pl.edu.pw.manager.service.mapper.ServicePasswordMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterUserDTO user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        new RegisterUserValidator().validate(user);

        userRepository.save(new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                passwordEncoder.encode(user.getMasterPassword())
        ));
    }

    @Override
    public void addNewPassword(String username, NewServicePasswordDTO newPassword) throws Exception {
        User user = getByUsername(username);

        validateUser(user, newPassword.getMasterPassword());
        new NewServicePasswordValidator().validate(newPassword);

        saveNewPassword(user, newPassword);
    }

    private void saveNewPassword(User user, NewServicePasswordDTO newPassword) throws Exception {
        user.getPasswords().add(new ServicePassword(
                newPassword.getServiceName(),
                new AESAdapter().encrypt(newPassword.getPassword(), newPassword.getMasterPassword())
        ));
        userRepository.save(user);
    }

    @Override
    public List<ServicePasswordDTO> getServicePasswords(String username) {
        User user = userRepository.findByUsername(username);

        if(user != null) {
            return new ServicePasswordMapper().map(user.getPasswords());
        } else {
            throw new IllegalArgumentException(username + " does not exist");
        }
    }

    @Override
    public ServicePasswordDTO getServicePassword(String username, Long servicePasswordId, String masterPassword) throws Exception {
        User user = getByUsername(username);

        validateUser(user, masterPassword);

        return getServicePassword(user, servicePasswordId, masterPassword);
    }

    private ServicePasswordDTO getServicePassword(User user, Long id, String masterPassword) throws Exception {
        List<ServicePassword> passwords = user.getPasswords();
        for(ServicePassword p: passwords) {
            if (p.getId().equals(id)) {
                return new ServicePasswordDTO(
                        p.getId(),
                        p.getServiceName(),
                        new AESAdapter().decrypt(p.getPassword(), masterPassword)
                );
            }
        }
        throw new AccessControlException("Password not found in " + user.getUsername() + " passwords");
    }

    @Override
    public void deletePassword(String username, Long servicePasswordId, String masterPassword) {
        User user = getByUsername(username);

        validateUser(user, masterPassword);

        deletePassword(user, servicePasswordId);
    }

    private void deletePassword(User user, Long id) {
        List<ServicePassword> passwords = user.getPasswords();
        boolean removed = passwords.removeIf((p) -> (p.getId().equals(id)));

        if (removed) {
            userRepository.save(user);
        } else {
            throw new AccessControlException("Password not found in " + user.getUsername() + " passwords");
        }
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void increaseFailedAttempts(User user) {
        user.setFailedAttempt(user.getFailedAttempt() + 1);
        userRepository.save(user);
    }

    @Override
    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());

        userRepository.save(user);
    }

    @Override
    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);

            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resetFailedAttempts(User user) {
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    private void validateUser(User user, String masterPassword) {
        if (user != null) {
            if (user.getAccountNonLocked() || unlockWhenTimeExpired(user)) {
                if (!passwordEncoder.matches(masterPassword, user.getMasterPassword())) {
                    if (user.getFailedAttempt() < MAX_FAILED_ATTEMPTS - 1) {
                        increaseFailedAttempts(user);
                        throw new IllegalArgumentException("Wrong master password");
                    } else {
                        lock(user);
                        throw new IllegalArgumentException("Your account has been locked due to " + MAX_FAILED_ATTEMPTS
                                + " failed attempts. It will be unlocked after 1 minute.");
                    }
                } else {
                    resetFailedAttempts(user);
                }
            } else {
                throw new IllegalArgumentException("Your account is locked. Try again later");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

}
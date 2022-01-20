package pl.edu.pw.manager.service;

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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

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
    public void addNewPassword(String username, NewServicePasswordDTO newPassword) throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeySpecException, InvalidKeyException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException(username + " does not exist");
        }
        new NewServicePasswordValidator().validate(newPassword);

        if (passwordEncoder.matches(newPassword.getMasterPassword(), user.getMasterPassword())) {
            saveNewPassword(user, newPassword);
        } else {
            throw new IllegalArgumentException("Master password is wrong");
        }
    }

    private void saveNewPassword(User user, NewServicePasswordDTO newPassword) throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeySpecException, InvalidKeyException {

        user.getPasswords().add(new ServicePassword(
                newPassword.getServiceName(),
                new AESAdapter().encrypt(newPassword.getPassword(), newPassword.getMasterPassword())
        ));
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
}

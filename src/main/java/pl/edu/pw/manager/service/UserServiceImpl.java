package pl.edu.pw.manager.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.manager.domain.User;
import pl.edu.pw.manager.dto.RegisterUserDTO;
import pl.edu.pw.manager.repository.UserRepository;
import pl.edu.pw.manager.security.validation.RegisterUserValidator;

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

}

package pl.edu.pw.manager.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.manager.domain.ServicePassword;
import pl.edu.pw.manager.domain.User;
import pl.edu.pw.manager.security.cipher.AESAdapter;

import java.util.ArrayList;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        this.userRepository.deleteAll();
        AESAdapter aes = new AESAdapter();

        User dan = new User();
        dan.setUsername("Maciek");
        dan.setPassword(passwordEncoder.encode("123"));
        dan.setMasterPassword(passwordEncoder.encode("321"));

        List<ServicePassword> passwords = new ArrayList<>();
        try {
            passwords.add(new ServicePassword(null, "Facebook", aes.encrypt("haslo", "321")));
            passwords.add(new ServicePassword(null, "Twitter", aes.encrypt("haslo", "321")));
            passwords.add(new ServicePassword(null, "Isod", aes.encrypt("haslo", "321")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        dan.setPasswords(passwords);

        this.userRepository.save(dan);
    }
}

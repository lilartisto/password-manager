package pl.edu.pw.manager.security.validation;

import org.passay.*;
import pl.edu.pw.manager.dto.RegisterUserDTO;

import java.util.Arrays;

public class RegisterUserValidator {

    public void validate(RegisterUserDTO user) {
        checkIfFieldsAreEmpty(user);
        checkPasswordsEquals(user);
        checkPasswordsStrength(user);
    }

    private void checkIfFieldsAreEmpty(RegisterUserDTO user) {
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getMasterPassword().isEmpty()) {
            throw new IllegalArgumentException("User data cannot be empty");
        }
    }

    private void checkPasswordsEquals(RegisterUserDTO user) {
        String password = user.getPassword();
        String masterPassword = user.getMasterPassword();

        if(!password.equals(user.getPasswordRepeat())) {
            throw new IllegalArgumentException("Passwords are not equal");
        } else if(!masterPassword.equals(user.getMasterPasswordRepeat())) {
            throw new IllegalArgumentException("Master passwords are not equal");
        } else if(password.equals(masterPassword)) {
            throw new IllegalArgumentException("Account password and master password cannot be equal");
        }
    }

    private void checkPasswordsStrength(RegisterUserDTO user) {
        PasswordValidator validator = passwordValidator();

        if(!validator.validate(new PasswordData(user.getPassword())).isValid()) {
            throw new IllegalArgumentException("Password is not strong enough." +
                    "Password length: 7-30, at least one: uppercase, digit, special and alphabetical character");
        } else if(!validator.validate(new PasswordData(user.getMasterPassword())).isValid()) {
            throw new IllegalArgumentException("Master password is not strong enough." +
                    "Use password with length: 7-30, at least one uppercase, digit, special and alphabetical character");
        }
    }

    private PasswordValidator passwordValidator() {
        return new PasswordValidator(Arrays.asList(
                new LengthRule(7, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new AlphabeticalCharacterRule(1)
        ));
    }

}

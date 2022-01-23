package pl.edu.pw.manager.security.validation;

import pl.edu.pw.manager.dto.NewServicePasswordDTO;

public class NewServicePasswordValidator {

    public void validate(NewServicePasswordDTO servicePassword) {
        if (servicePassword.getServiceName() == null || servicePassword.getPassword() == null ||
                servicePassword.getPasswordRepeat() == null || servicePassword.getMasterPassword() == null) {
            throw new IllegalArgumentException("New password cannot contain null fields");
        } else {
            checkLengths(servicePassword);
            checkPasswordEquality(servicePassword);
        }
    }

    private void checkLengths(NewServicePasswordDTO servicePassword) {
        if (servicePassword.getServiceName().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be empty");
        } else if (servicePassword.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        } else if (servicePassword.getPasswordRepeat().isEmpty()) {
            throw new IllegalArgumentException("Repeated password cannot be empty");
        } else if (servicePassword.getMasterPassword().isEmpty()) {
            throw new IllegalArgumentException("Master password cannot be empty");
        }
    }

    private void checkPasswordEquality(NewServicePasswordDTO servicePassword) {
        if (!servicePassword.getPassword().equals(servicePassword.getPasswordRepeat())) {
            throw new IllegalArgumentException("Passwords are not the same");
        }
    }

}

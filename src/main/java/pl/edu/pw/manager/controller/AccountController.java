package pl.edu.pw.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.edu.pw.manager.dto.RegisterUserDTO;
import pl.edu.pw.manager.service.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class AccountController {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/register")
    public String registerView(Model model) {
        model.addAttribute("user", new RegisterUserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Model model, @ModelAttribute("user") RegisterUserDTO user) {
        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e.getCause());
            model.addAttribute("error", "Unexpected error has occurred");
            return "register";
        }
    }

}

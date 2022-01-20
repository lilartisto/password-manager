package pl.edu.pw.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.edu.pw.manager.dto.NewServicePasswordDTO;
import pl.edu.pw.manager.dto.ServicePasswordDTO;
import pl.edu.pw.manager.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homeView(Model model, Principal principal) {
        List<ServicePasswordDTO> servicePasswords = userService.getServicePasswords(principal.getName());
        model.addAttribute("servicePasswords", servicePasswords);
        return "passwords_list";
    }

    @GetMapping("/addpassword")
    public String addPasswordView(Model model) {
        model.addAttribute("newServicePassword", new NewServicePasswordDTO());
        return "add_password";
    }

    @PostMapping("/addpassword")
    public String addPassword(Model model, Principal principal,
                              @ModelAttribute("newServicePassword") NewServicePasswordDTO newPassword) {
        try {
            userService.addNewPassword(principal.getName(), newPassword);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "add_password";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error has occurred");
            return "add_password";
        }
    }

}

package pl.edu.pw.manager.controller;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.edu.pw.manager.dto.NewServicePasswordDTO;
import pl.edu.pw.manager.dto.ServicePasswordDTO;
import pl.edu.pw.manager.dto.TextDTO;
import pl.edu.pw.manager.service.UserService;

import java.nio.file.AccessDeniedException;
import java.security.AccessControlException;
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

    @GetMapping("/show/{id}")
    public String showPasswordView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("masterPassword", new TextDTO());
        model.addAttribute("postURL", "/show/" + id);
        return "master_password_form";
    }

    @PostMapping("/show/{id}")
    public String showPassword(Model model, Principal principal, @PathVariable("id") Long id,
                               @ModelAttribute("masterPassword") TextDTO masterPassword) {
        try {
            ServicePasswordDTO password = userService.getServicePassword(principal.getName(), id, masterPassword.getValue());
            model.addAttribute("password", password);
            return "show_service_password";
        } catch (AccessControlException e) {
            model.addAttribute("error", e.getMessage() + ". Please check your URL");
            return "master_password_form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "master_password_form";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error has occurred");
            return "master_password_form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePasswordView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("masterPassword", new TextDTO());
        model.addAttribute("postURL", "/delete/" + id);
        return "master_password_form";
    }

    @PostMapping("/delete/{id}")
    public String deletePassword(Model model, Principal principal, @PathVariable("id") Long id,
                                 @ModelAttribute("masterPassword") TextDTO masterPassword) {
        try {
            userService.deletePassword(principal.getName(), id, masterPassword.getValue());
            return "redirect:/";
        } catch (AccessControlException e) {
            model.addAttribute("error", e.getMessage() + ". Please check your URL");
            return "master_password_form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "master_password_form";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error has occurred");
            return "master_password_form";
        }
    }

}

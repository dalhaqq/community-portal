package com.abcjobs.communityportal.controllers;

import com.abcjobs.communityportal.forms.RegistrationForm;
import com.abcjobs.communityportal.models.User;
import com.abcjobs.communityportal.repositories.UserProfileRepository;
import com.abcjobs.communityportal.repositories.UserRepository;
import com.abcjobs.communityportal.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;

    @Autowired
    public AuthController(UserService userService, UserProfileRepository userProfileRepository) {
        this.userService = userService;
        this.userProfileRepository = userProfileRepository;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String disabled, @RequestParam(required = false) String verified, @RequestParam(required = false) String invalid, @RequestParam(required = false) String thanks, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (disabled != null) {
            model.addAttribute("error", "Please verify your email address");
            model.addAttribute("disabled", true);
        }
        if (verified != null) {
            model.addAttribute("success", "Email verified successfully");
        }
        if (invalid != null) {
            model.addAttribute("error", "Invalid verification token");
            model.addAttribute("disabled", true);
        }
        if (thanks != null) {
            model.addAttribute("success", "Thanks for registering! Please check your email to verify your account");
            model.addAttribute("disabled", true);
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(RegistrationForm registrationForm) {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid RegistrationForm registrationForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (userService.findByEmail(registrationForm.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "This email is already in use");
            return "register";
        }

        if (!registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "register";
        }

        User user = userService.createUser(registrationForm);

        return "redirect:/login?thanks";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String email, @RequestParam String token, Model model) {
        User user = userRepository.findByEmail(email);
        if (user.getVerificationToken().equals(token)) {
            user.setVerified(true);
            userRepository.save(user);
            return "redirect:/login?verified";
        }
        return "redirect:/login?invalid";
    }
}

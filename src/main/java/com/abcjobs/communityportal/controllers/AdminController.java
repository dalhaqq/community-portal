package com.abcjobs.communityportal.controllers;

import com.abcjobs.communityportal.models.User;
import com.abcjobs.communityportal.repositories.UserRepository;
import com.abcjobs.communityportal.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

@Controller
public class AdminController {
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public String index(Authentication authentication, Model model) {
        if (userAuthService.isLoggedIn()) {
            String username = authentication.getName();
            User user = userRepository.findByEmail(username);
            if (user.isAdmin()) {
                Collection<User> users = (Collection<User>) userRepository.findAll();
                model.addAttribute("users", users);
                return "admin";
            }
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable String id, Authentication authentication, Model model) {
        if (userAuthService.isLoggedIn()) {
            String username = authentication.getName();
            User user = userRepository.findByEmail(username);
            if (user.isAdmin()) {
                User userToEdit = userRepository.findById(Integer.parseInt(id)).orElse(null);
                model.addAttribute("user", userToEdit);
                return "edit";
            }
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable String id, Authentication authentication) {
        if (userAuthService.isLoggedIn()) {
            String username = authentication.getName();
            User user = userRepository.findByEmail(username);
            if (user.isAdmin()) {
                User userToDelete = userRepository.findById(Integer.parseInt(id)).orElse(null);
                int userProfileId = userToDelete.getUserProfile().getId();
                userRepository.delete(userToDelete);

                return "redirect:/admin";
            }
            return "redirect:/";
        }
        return "redirect:/login";
    }
}

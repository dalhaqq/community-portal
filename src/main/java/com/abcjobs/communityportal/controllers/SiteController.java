package com.abcjobs.communityportal.controllers;

import com.abcjobs.communityportal.forms.ProfileUpdateForm;
import com.abcjobs.communityportal.models.User;
import com.abcjobs.communityportal.models.UserProfile;
import com.abcjobs.communityportal.repositories.UserProfileRepository;
import com.abcjobs.communityportal.repositories.UserRepository;
import com.abcjobs.communityportal.services.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class SiteController {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (userAuthService.isLoggedIn()) {
            String username = authentication.getName();
            User user = userRepository.findByEmail(username);
            if (user.isAdmin()) {
                return "redirect:/admin";
            }
            return "index";
        }
        return "landing";
    }

    @GetMapping("/profile/{id}")
    public ModelAndView profile(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        UserProfile userProfile = user != null ? user.getUserProfile() : null;
        modelAndView.addObject("userProfile", userProfile);
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam String query) {
        Collection<UserProfile> userProfiles;
        if (query == null || query.trim() == "") {
            userProfiles = (Collection<UserProfile>) userProfileRepository.findAll();
        } else {
            userProfiles = userProfileRepository.searchUserProfile(query);
        }
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("userProfiles", userProfiles);
        modelAndView.addObject("query", query);
        if (userProfiles.size() == 0) {
            modelAndView.addObject("noResults", true);
        } else {
            modelAndView.addObject("noResults", false);
        }
        return modelAndView;
    }

    @GetMapping("/update-profile")
    public ModelAndView updateProfile(ProfileUpdateForm profileUpdateForm, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);
        UserProfile userProfile = user.getUserProfile();
        ModelAndView modelAndView = new ModelAndView("update-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userProfile", userProfile);
        return modelAndView;
    }

    @PostMapping("/update-profile")
    public String updateProfilePost(@Valid ProfileUpdateForm profileUpdateForm, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "update-profile";
        }

        String username = authentication.getName();
        User user = userRepository.findByEmail(username);
        UserProfile userProfile = user.getUserProfile();
        userProfile.setFullName(profileUpdateForm.getFullName());
        userProfile.setJob(profileUpdateForm.getJob());
        userProfile.setLocation(profileUpdateForm.getLocation());
        userProfile.setEducation(profileUpdateForm.getEducation());
        userProfile.setPhone(profileUpdateForm.getPhone());
        userProfileRepository.save(userProfile);
        return "redirect:/profile/" + user.getId();
    }

}

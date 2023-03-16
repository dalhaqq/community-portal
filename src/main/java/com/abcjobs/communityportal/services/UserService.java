package com.abcjobs.communityportal.services;

import com.abcjobs.communityportal.forms.RegistrationForm;
import com.abcjobs.communityportal.models.User;
import com.abcjobs.communityportal.models.UserProfile;
import com.abcjobs.communityportal.repositories.UserProfileRepository;
import com.abcjobs.communityportal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userProfileRepository = userProfileRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(RegistrationForm registrationForm) {
        User user = new User();
        user.setEmail(registrationForm.getEmail());
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        String resetToken = passwordEncoder.encode(user.getEmail());
        String verifyToken = passwordEncoder.encode(user.getEmail());
        user.setResetToken(resetToken);
        user.setVerificationToken(verifyToken);

        user.setAdmin(false);
        user.setVerified(false);

        user = userRepository.save(user);

        sendEmailVerification(user);

        UserProfile userProfile = new UserProfile();
        userProfile.setFullName(registrationForm.getFullName());
        userProfile.setUser(user);
        userProfile.setEducation("N/A");
        userProfile.setLocation("N/A");
        userProfile.setJob("N/A");
        userProfile.setPhone("N/A");
        userProfileRepository.save(userProfile);

        user.setUserProfile(userProfile);
        userRepository.save(user);
        return user;
    }

    public void sendEmailVerification(User user) {
        String email = user.getEmail();
        String token = user.getVerificationToken();
        String url = "http://localhost:8080/verify?email=" + email + "&token=" + token;
        String subject = "Verify your email";
        String body = "Please click the link to verify your email address: <a href='%s'>%s</a>".formatted(url, url);

        this.emailService.sendEmail(email, subject, body);
    }

    public void sendPasswordResetToken(User user) {
        String email = user.getEmail();
        String token = user.getResetToken();
        String url = "http://localhost:8080/reset?email=" + email + "&token=" + token;
        String subject = "Reset your password";
        String body = "Please click the link to reset your password: <a href='%s'>%s</a>".formatted(url, url);

        this.emailService.sendEmail(email, subject, body);
    }

    public void resetPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}

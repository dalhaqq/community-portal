package com.abcjobs.communityportal.repositories;

import com.abcjobs.communityportal.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

    User findByResetToken(String resetToken);

    User findByVerificationToken(String verificationToken);
}

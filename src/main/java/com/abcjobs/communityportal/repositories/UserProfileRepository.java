package com.abcjobs.communityportal.repositories;

import com.abcjobs.communityportal.models.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {
    @Query("SELECT u FROM UserProfile u WHERE u.searchString LIKE '%' || LOWER(:query) || '%'")
    Collection<UserProfile> searchUserProfile(@Param("query") String query);
}

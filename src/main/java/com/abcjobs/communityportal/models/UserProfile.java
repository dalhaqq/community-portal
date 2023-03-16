package com.abcjobs.communityportal.models;

import jakarta.persistence.*;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "userProfile")
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Column
    private String phone;

    @Column
    private String location;

    @Column
    private String education;

    @Column
    private String job;

    @Column(length = 1000)
    private String searchString;

    @PreUpdate
    @PrePersist
    void updateSearchString() {
        final String fullSearchString = StringUtils.join(Arrays.asList(
                        fullName.toLowerCase(),
                        job.toLowerCase(),
                        location.toLowerCase()),
                " ");
        if (fullSearchString.length() > 1000) {
            this.searchString = StringUtils.substring(fullSearchString, 0, 999);
        } else {
            this.searchString = fullSearchString;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}

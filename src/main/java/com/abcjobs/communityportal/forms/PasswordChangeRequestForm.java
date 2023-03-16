package com.abcjobs.communityportal.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class PasswordChangeRequestForm {
    @NotNull
    @Email()
    private String email;
}

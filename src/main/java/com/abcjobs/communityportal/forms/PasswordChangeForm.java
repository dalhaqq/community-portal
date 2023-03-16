package com.abcjobs.communityportal.forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PasswordChangeForm {
    @NotNull
    @Size(min = 8, max = 20)
    private String oldPassword;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Size(min = 8, max = 20)
    private String confirmPassword;
}

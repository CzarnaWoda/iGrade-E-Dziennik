package me.igrade.user.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TeacherRegisterRequest(

        @NotBlank(message = "Name can not be blank")
        @NotNull(message = "Name can not be null")
        @Size(min = 6, max = 28, message = "Name must be between 6 and 28 length")
        String firstName,

        @NotBlank(message = "Last name can not be blank")
        @NotNull(message = "Last name can not be null")
        @Size(min = 6, max = 28, message = "Last name must be between 6 and 28 length")
        String lastName,

        @NotBlank(message = "Password can not be blank")
        @NotNull(message = "Password name can not be null")
        @Size(min = 6, max = 28, message = "Password name must be between 6 and 28 length")
        String password,

        @Pattern(regexp = ".*@.*\\..*")
        @Size(min = 11, max = 48, message = "Email must be between 11 and 48 length")
        String email
) {
}
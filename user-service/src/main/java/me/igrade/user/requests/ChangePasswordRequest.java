package me.igrade.user.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @Size(min = 6, max = 28, message = "Password must be between 6 and 28 length")
        @NotNull(message = "Password can not be null")
        @NotBlank(message = "Password name can not be null")
        String accountPassword,
        @NotBlank(message = "Password can not be blank")
        @NotNull(message = "Password name can not be null")
        @Size(min = 6, max = 28, message = "Password name must be between 6 and 28 length")
        String password,
        @NotBlank(message = "Password can not be blank")
        @NotNull(message = "Password name can not be null")
        @Size(min = 6, max = 28, message = "Password name must be between 6 and 28 length")
        String repeatPassword
) {
}

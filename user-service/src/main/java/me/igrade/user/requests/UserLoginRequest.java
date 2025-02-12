package me.igrade.user.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(

    @Pattern(regexp = ".*@.*\\..*")
    @Size(min = 11, max = 48, message = "Email must be between 11 and 48 length")
    String email,
    @NotBlank(message = "Password can not be blank")
    @NotNull(message = "Password name can not be null")
    @Size(min = 6, max = 28, message = "Password name must be between 6 and 28 length")
    String password,

    boolean remember

){
}

package me.igrade.schoolclass.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateClassRequest(
        @Length(min = 1, max = 6)
        String className,
        int teacherId

) {
}

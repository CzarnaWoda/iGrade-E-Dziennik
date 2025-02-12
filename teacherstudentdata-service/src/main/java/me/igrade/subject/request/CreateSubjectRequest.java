package me.igrade.subject.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateSubjectRequest(

        @NotNull
        @NotBlank
        @Length(min = 4, max = 25)
        String subjectName,
        @NotNull
        int teacherId,
        @NotNull
        long classId
) {
}

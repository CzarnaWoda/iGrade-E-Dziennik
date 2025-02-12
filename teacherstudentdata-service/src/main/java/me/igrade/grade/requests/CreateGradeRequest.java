package me.igrade.grade.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGradeRequest(
        @NotNull
        @DecimalMin(value = "0")
        @DecimalMax(value = "6")
        int grade,
        @NotNull
        int teacherId,
        @NotNull
        int studentId,
        @NotNull
        long subjectId
) {
}

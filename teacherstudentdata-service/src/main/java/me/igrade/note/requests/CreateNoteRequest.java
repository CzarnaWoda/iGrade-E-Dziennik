package me.igrade.note.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateNoteRequest(
        @DecimalMin(value = "-100")
        @DecimalMax(value = "100")
        @NotNull
        int points,
        @NotNull
        int studentId,
        @NotNull
        int teacherId,
        @NotNull
        String description
) {
}

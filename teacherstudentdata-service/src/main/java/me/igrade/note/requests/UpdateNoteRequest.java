package me.igrade.note.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UpdateNoteRequest(
        @DecimalMin(value = "-100")
        @DecimalMax(value = "100")
        @NotNull
        int points,
        @NotNull
        String description
) {


}

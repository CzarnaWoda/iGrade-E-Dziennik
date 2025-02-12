package me.igrade.schoolclass.request;

import org.hibernate.validator.constraints.Length;

public record UpdateClassRequest(
        @Length(min = 1, max = 6)
        String className,
        @Length(min = 4, max = 32)
        String classCode

) {
}

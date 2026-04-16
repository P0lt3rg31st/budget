package ru.utmn.budget.specdto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank
        @Size(min = 8, max = 255)
        String currentPassword,

        @NotBlank
        @Size(min = 8, max = 255)
        String newPassword
) {
}

package ru.utmn.budget.specdto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

        @NotBlank
        @Email
        @Size(max = 254)
        String email,

        @NotBlank
        @Size(min = 1, max = 120)
        String displayName,

        @NotBlank
        @Size(min = 8, max = 255)
        String password
) {
}

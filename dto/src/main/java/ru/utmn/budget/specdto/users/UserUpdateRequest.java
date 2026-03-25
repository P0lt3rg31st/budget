package ru.utmn.budget.specdto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        @Email
        @Size(max = 254)
        String email,

        @Size(min = 1, max = 120)
        String displayName
) {
}
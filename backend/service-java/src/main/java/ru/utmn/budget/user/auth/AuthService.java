package ru.utmn.budget.user.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.utmn.budget.handler.UnauthorizedException;
import ru.utmn.budget.model.domain.User;
import ru.utmn.budget.specdto.auth.AuthTokenResponse;
import ru.utmn.budget.user.UserRepository;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthProperties authProperties;

    public AuthTokenResponse login(String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new UnauthorizedException("Неверный email или пароль."));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new UnauthorizedException("Неверный email или пароль.");
        }

        String accessToken = jwtService.generateAccessToken(user);

        return new AuthTokenResponse(
                "Bearer",
                accessToken,
                authProperties.accessTokenExpiresInSeconds()
        );
    }

    private String normalizeEmail(String email) {
        return email.toLowerCase(Locale.ROOT);
    }
}
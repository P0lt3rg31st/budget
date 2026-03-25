package ru.utmn.budget.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.utmn.budget.handler.BadRequestException;
import ru.utmn.budget.handler.ConflictException;
import ru.utmn.budget.handler.NotFoundException;
import ru.utmn.budget.model.domain.User;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getById(Long userId) {
        return findUserById(userId);
    }

    public User getByEmailIgnoreCase(String email) {

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User not found: email=" + email));
    }

    public boolean existsByEmailIgnoreCase(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Transactional
    public User create(String email, String displayName, String rawPassword) {
        validateEmailUniqueForCreate(email);

        User user = new User();
        user.setEmail(email);
        user.setDisplayName(displayName);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Email already exists: " + email);
        }
    }

    @Transactional
    public User update(Long userId, String email, String displayName) {
        User user = findUserById(userId);

        if (email != null) {
            validateEmailUniqueForUpdate(email, userId);
            user.setEmail(email);
        }

        if (displayName != null) {
            user.setDisplayName(displayName);
        }

        return user;
    }

    @Transactional
    public void changePassword(Long userId, String currentRawPassword, String newRawPassword) {
        User user = findUserById(userId);

        if (!passwordEncoder.matches(currentRawPassword, user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (passwordEncoder.matches(newRawPassword, user.getPasswordHash())) {
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPasswordHash(passwordEncoder.encode(newRawPassword));
    }

    @Transactional
    public void delete(Long userId) {
        User user = findUserById(userId);

        validateUserCanBeDeleted(user);

        userRepository.delete(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: id=" + userId));
    }

    private void validateEmailUniqueForCreate(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Email already exists: " + email);
        }
    }

    private void validateEmailUniqueForUpdate(String email, Long userId) {
        if (userRepository.existsByEmailIgnoreCaseAndIdNot(email, userId)) {
            throw new ConflictException("Email already exists: " + email);
        }
    }

    // TODO: FIX
    private void validateUserCanBeDeleted(User user) {
        if (!user.getAccounts().isEmpty()
                || !user.getCategories().isEmpty()
                || !user.getBudgetPlans().isEmpty()
                || !user.getForecastRuns().isEmpty()
                || !user.getAlerts().isEmpty()
                || !user.getScenarios().isEmpty()
                || !user.getImportJobs().isEmpty()) {
            throw new BadRequestException(
                    "User cannot be deleted because related data exists: id=" + user.getId()
            );
        }
    }
}
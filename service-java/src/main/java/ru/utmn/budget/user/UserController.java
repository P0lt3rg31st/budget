package ru.utmn.budget.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.utmn.budget.model.domain.User;
import ru.utmn.budget.specdto.users.ChangePasswordRequest;
import ru.utmn.budget.specdto.users.UserCreateRequest;
import ru.utmn.budget.specdto.users.UserDto;
import ru.utmn.budget.specdto.users.UserUpdateRequest;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateRequest request) {
        User createdUser = userService.create(
                request.email(),
                request.displayName(),
                request.password()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toDto(createdUser));
    }

    @GetMapping("/me")
    public UserDto getMe(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("user_id");
        User user = userService.getById(userId);
        return userMapper.toDto(user);
    }

    @PatchMapping("/me")
    public UserDto updateMe(@AuthenticationPrincipal(expression = "id") Long currentUserId,
                            @Valid @RequestBody UserUpdateRequest request) {
        User updatedUser = userService.update(
                currentUserId,
                request.email(),
                request.displayName()
        );

        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal(expression = "id") Long currentUserId) {
        userService.delete(currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @AuthenticationPrincipal(expression = "id") Long currentUserId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(
                currentUserId,
                request.currentPassword(),
                request.newPassword()
        );

        return ResponseEntity.noContent().build();
    }
}
package com.qcommerce.backend.controller;


import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.dto.request.ChangePasswordRequest;
import com.qcommerce.backend.dto.request.UpdateProfileRequest;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ── Profile ───────────────────────────────────────────────────────────────

    @GetMapping("/users/profile")
    public UserResponse getProfile(@RequestAttribute String userId) {
        return userService.getProfile(userId);
    }

    @PutMapping("/users/profile")
    public UserResponse updateProfile(@RequestAttribute String userId,
                                       @Valid @RequestBody UpdateProfileRequest request) {

        return userService.updateProfile(userId, request);
    }

    // ── Credentials ───────────────────────────────────────────────────────────

    @PatchMapping("/users/profile/password")
    public UserResponse changePassword( @RequestAttribute String userId,
                                        @Valid @RequestBody ChangePasswordRequest request) {

        return userService.changePassword(userId, request);
    }

    // ── Admin ─────────────────────────────────────────────────────────────────

    @GetMapping("/admin/users")
    public PageResponse<UserResponse> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)   int pageSize) {

        return userService.getUsers(search, pageNumber, pageSize);
    }

    @GetMapping("/admin/users/{userId}")
    public UserResponse getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

    @PatchMapping("/admin/users/{userId}/enable")
    public UserResponse enableUser(@PathVariable String userId) {
        return userService.toggleUserStatus(userId, true);
    }

    @PatchMapping("/admin/users/{userId}/disable")
    public UserResponse disableUser(@PathVariable String userId) {
        return userService.toggleUserStatus(userId, false);
    }
}

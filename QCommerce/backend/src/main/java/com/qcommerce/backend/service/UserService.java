package com.qcommerce.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.dto.request.ChangePasswordRequest;
import com.qcommerce.backend.dto.request.UpdateProfileRequest;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.entity.helper.AddressInfo;
import com.qcommerce.backend.entity.helper.ContactInfo;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.PageMapper;
import com.qcommerce.backend.mapper.UserMapper;
import com.qcommerce.backend.repository.UserRepository;
import com.qcommerce.backend.util.FileStorageUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Profile ───────────────────────────────────────────────────────────────

    public UserResponse getProfile(String userId) {
        return UserMapper.toResponse(findByIdOrThrow(userId));
    }

    public UserResponse updateProfile(String userId, UpdateProfileRequest request) {
        UserEntity user = findByIdOrThrow(userId);

        // Only update fields that were actually sent (non-null)
        ContactInfo contact = user.getContactInfo();
        if (request.fullName() != null) {
            contact.setFullName(request.fullName());
        }
        if (request.phoneNumber() != null) {
            contact.setPhoneNumber(request.phoneNumber());
        }
        user.setContactInfo(contact);

        AddressInfo address = user.getAddressInfo();
        if (request.address() != null) {
            address.setAddress(request.address());
        }
        if (request.city() != null) {
            address.setCity(request.city());
        }
        if (request.state() != null) {
            address.setState(request.state());
        }
        if (request.zipCode() != null) {
            address.setZipCode(request.zipCode());
        }
        user.setAddressInfo(address);

        return UserMapper.toResponse(userRepository.save(user));
    }

    // ── Credentials ───────────────────────────────────────────────────────────

    /**
     * Changes the user's password.
     * Verifies the current password and ensures newPassword == confirmPassword.
     */
    public UserResponse changePassword(String userId, ChangePasswordRequest request) {
        UserEntity user = findByIdOrThrow(userId);

        // Verify current password
        if (!passwordEncoder.matches(request.currentPassword(), user.getUserPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Confirm the two new-password fields match
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        user.setUserPassword(passwordEncoder.encode(request.newPassword()));
        return UserMapper.toResponse(userRepository.save(user));
    }

    // ── Admin operations ──────────────────────────────────────────────────────

    public PageResponse<UserResponse> getUsers(String search, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("userEmail"));
        Page<UserEntity> userPage = userRepository.searchUsers(search, pageable);
        Page<UserResponse> responsePage = userPage.map(UserMapper::toResponse);
        return PageMapper.toResponse(responsePage);
    }

    public UserResponse getUserById(String userId) {
        return UserMapper.toResponse(findByIdOrThrow(userId));
    }

    public void deleteUser(String userId) {
        UserEntity user = findByIdOrThrow(userId);
        // Clean up profile image from disk before deleting the record
        if (user.getProfileImage() != null) {
            FileStorageUtil.deleteFile(user.getProfileImage(), AppConstants.UPLOAD_DIR_PROFILE_IMAGE);
        }
        userRepository.deleteById(userId);
    }

    public UserResponse toggleUserStatus(String userId, boolean enable) {
        UserEntity user = findByIdOrThrow(userId);
        user.setEnabled(enable);
        return UserMapper.toResponse(userRepository.save(user));
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private UserEntity findByIdOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

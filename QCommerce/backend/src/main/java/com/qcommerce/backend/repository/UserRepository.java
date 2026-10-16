package com.qcommerce.backend.repository;

import com.qcommerce.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserEmail(String userEmail);

    Optional<UserEntity> findByUserEmail(String userEmail);

    @Query("""
        SELECT u FROM UserEntity u
        WHERE
            (:search IS NULL OR :search = ''
             OR LOWER(u.userEmail) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(u.contactInfo.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<UserEntity> searchUsers(String search, Pageable pageable);
}

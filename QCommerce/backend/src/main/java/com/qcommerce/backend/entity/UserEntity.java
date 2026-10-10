package com.qcommerce.backend.entity;

import com.qcommerce.backend.entity.helper.AddressInfo;
import com.qcommerce.backend.entity.helper.ContactInfo;
import com.qcommerce.backend.entity.helper.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String userId;

    @Column(unique = true, nullable = false, length = 100)
    private String userEmail;

    @Column(nullable = false, length = 30)
    private String userPassword;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role userRole;

    private String profileImage;

    @Column(nullable = false)
    private boolean enabled = true;

    @Embedded
    private ContactInfo contactInfo;

    @Embedded
    private AddressInfo addressInfo;
}

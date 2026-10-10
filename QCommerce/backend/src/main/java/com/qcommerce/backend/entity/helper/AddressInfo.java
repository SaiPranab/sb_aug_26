package com.qcommerce.backend.entity.helper;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AddressInfo {
    private String address;

    private String city;

    private String state;

    private String zipCode;
}

package com.example.one_to_one;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    private String city;
    private String state;
    private String country;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Student student;
}

package com.ghtk.ecommercewebsite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String fullName;
    private String password;
    private String phone;
    private Boolean status;
    private String gender;
    private LocalDateTime createdAt;

    // Getters and setters
}

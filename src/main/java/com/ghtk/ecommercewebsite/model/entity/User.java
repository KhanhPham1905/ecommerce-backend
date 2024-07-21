package com.ghtk.ecommercewebsite.model.entity;

import com.ghtk.ecommercewebsite.model.enums.Gender;
import com.ghtk.ecommercewebsite.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "profit")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Role roles;

}

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
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", columnDefinition = "VARCHAR(50)")
    private String email;

    @Column(name = "full_name", columnDefinition = "VARCHAR(200)")
    private String fullName;

    @Column(name = "profit", columnDefinition = "DECIMAL(17,2)")
    private String password;

    @Column(name = "phone", columnDefinition = "VARCHAR(10)")
    private String phone;

    @Column(name = "status", columnDefinition = "TINY(1)")
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "modified_at", columnDefinition = "DECIMAL(17,2)")
    private Timestamp modifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Role roles;

}

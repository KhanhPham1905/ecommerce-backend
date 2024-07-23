package com.ghtk.ecommercewebsite.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(targetEntity = Role.class,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Long id;
//
//    @Column(name = "email", unique = true, nullable = false)
//    private String email;
//
//    @Column(name = "full_name", nullable = false)
//    private String fullName;
//
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    @Column(name = "phone")
//    private String phone;
//
//    @Column(name = "status")
//    private Boolean status;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "gender")
//    private Gender gender;
//
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private Date createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "modified_at")
//    private Date modifiedAt;
//
//    @ManyToOne
//    @Column(name = "role")
//    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
//    @JsonBackReference
//    private Role role;
}

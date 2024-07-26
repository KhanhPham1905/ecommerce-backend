//package com.ghtk.ecommercewebsite.models.entities;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import com.ghtk.ecommercewebsite.models.entities.User;
//import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.Fetch;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Table(name = "roles")
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Builder
//public class Role {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(nullable = false)
//    private Integer id;
//
//    @Column(unique = true, nullable = false)
//    @Enumerated(EnumType.STRING)
//    private RoleEnum name;
//
//    @Column(nullable = false)
//    private String description;
//
//    @ManyToMany(targetEntity = User.class, mappedBy = "roles",
//            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
//    @JsonBackReference
//    private Set<User> users;
//
//    @CreationTimestamp
//    @Column(updatable = false, name = "created_at")
//    private Date createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private Date updatedAt;
//
//}

package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleName name;

    public enum RoleName {
        ADMIN,
        SELLER,
        CUSTOMER
    }
}



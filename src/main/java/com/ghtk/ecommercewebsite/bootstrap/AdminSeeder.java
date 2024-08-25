package com.ghtk.ecommercewebsite.bootstrap;

import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import com.ghtk.ecommercewebsite.repositories.RoleRepository;
import com.ghtk.ecommercewebsite.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.entities.Role;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@DependsOn("roleSeeder")
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createAdmin();
    }

    public void createAdmin() {
        Optional<User> optionalAdmin = userRepository.findByEmail("admin@gmail.com");
        if (optionalAdmin.isPresent()) return;
        Optional<Role> optionalAdminRole = roleRepository.findByName(RoleEnum.ADMIN);
        if (optionalAdminRole.isEmpty()) return;

        var admin = User.builder()
                .fullName("Admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .roles(new HashSet<>(List.of(optionalAdminRole.get())))
                .build();
        userRepository.save(admin);

//        RegisterUserDto userDto = RegisterUserDto
//                .builder()
//                .fullName("Admin")
//                .email("admin@gmail.com")
//                .password("123456")
//                .build();
//        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
//        if (optionalRole.isEmpty()) return;
//        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
//        if (optionalUser.isPresent()) { return; }
//
//        var user = User
//                .builder()
//                .fullName(userDto.getFullName())
//                .email(userDto.getEmail())
//                .password(passwordEncoder.encode(userDto.getPassword()))
//                .roles(new HashSet<>(List.of(optionalRole.get())))
//                .build();
//        userRepository.save(user);
    }
}

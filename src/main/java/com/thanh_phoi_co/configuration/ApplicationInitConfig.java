package com.thanh_phoi_co.configuration;

import com.thanh_phoi_co.enums.Gender;
import com.thanh_phoi_co.enums.UserStatus;
import com.thanh_phoi_co.model.Role;
import com.thanh_phoi_co.model.User;
import com.thanh_phoi_co.repository.RoleRepository;
import com.thanh_phoi_co.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User user = User.builder()
                        .firstName("admin")
                        .lastName("admin")
                        .email("admin@gmail.com")
                        .phone("0987654321")
                        .gender(Gender.MALE)
                        .status(UserStatus.ACTIVE)
                        .username("admin")
                        .password(passwordEncoder.encode("password"))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user created with default password: password");
            }
        };
    }

}

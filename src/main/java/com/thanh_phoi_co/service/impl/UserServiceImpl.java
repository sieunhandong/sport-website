package com.thanh_phoi_co.service.impl;

import com.thanh_phoi_co.dto.response.PermissionResponse;
import com.thanh_phoi_co.dto.response.RoleResponse;
import com.thanh_phoi_co.dto.response.UserResponse;
import com.thanh_phoi_co.exception.ResourceNotFoundException;
import com.thanh_phoi_co.model.User;
import com.thanh_phoi_co.repository.UserRepository;
import com.thanh_phoi_co.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Email not found"));
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .gender(user.getGender())
                .status(user.getStatus())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(role -> new RoleResponse(role.getName(), role.getDescription(), role.getPermissions()
                                .stream()
                                .map(permission -> new PermissionResponse(permission.getName(), permission.getDescription()))
                                .collect(Collectors.toSet())))
                        .collect(Collectors.toSet()))
                .build();
    }
}

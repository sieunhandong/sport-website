package com.thanh_phoi_co.service;

import com.thanh_phoi_co.dto.request.UpdateUserRoleRequest;
import com.thanh_phoi_co.dto.response.PermissionResponse;
import com.thanh_phoi_co.dto.response.RoleResponse;
import com.thanh_phoi_co.dto.response.UserResponse;
import com.thanh_phoi_co.exception.AppException;
import com.thanh_phoi_co.exception.ErrorCode;
import com.thanh_phoi_co.mapper.UserMapper;
import com.thanh_phoi_co.model.Role;
import com.thanh_phoi_co.model.User;
import com.thanh_phoi_co.repository.RoleRepository;
import com.thanh_phoi_co.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public List<RoleResponse> getAllRole() {
        return roleRepository.findAll()
                .stream()
                .map(role -> RoleResponse.builder()
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(role.getPermissions().stream()
                                .map(permission -> new PermissionResponse(permission.getName(), permission.getDescription()))
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional
    public UserResponse updateUserRole(UpdateUserRoleRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(request.getRoleNames()));

        if (newRoles.size() != request.getRoleNames().size()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        user.setRoles(newRoles);   // Bây giờ có thể thử lại cách này

        return userMapper.toUserResponse(userRepository.save(user));
    }

}

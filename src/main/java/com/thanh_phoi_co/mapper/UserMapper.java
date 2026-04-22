package com.thanh_phoi_co.mapper;

import com.thanh_phoi_co.dto.request.SignUpRequest;
import com.thanh_phoi_co.dto.response.UserResponse;
import com.thanh_phoi_co.model.Role;
import com.thanh_phoi_co.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(request.getRoles()))")
    User toUser(SignUpRequest request);

    UserResponse toUserResponse(User user);

    default Set<Role> mapRoles(List<String> roleNames){
        if(roleNames == null){
            return new HashSet<>();
        }
        return roleNames.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toSet());
    }
}

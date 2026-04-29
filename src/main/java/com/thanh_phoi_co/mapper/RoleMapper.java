package com.thanh_phoi_co.mapper;

import com.thanh_phoi_co.dto.response.RoleResponse;
import com.thanh_phoi_co.model.Role;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);

    Set<RoleResponse> toRoleResponses(Set<Role> roles);
}

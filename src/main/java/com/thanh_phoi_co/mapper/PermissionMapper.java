package com.thanh_phoi_co.mapper;

import com.thanh_phoi_co.dto.response.PermissionResponse;
import com.thanh_phoi_co.model.Permission;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);

    Set<PermissionResponse> toPermissionResponses(Set<Permission> permissions);
}

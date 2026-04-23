package com.thanh_phoi_co.mapper;

import com.thanh_phoi_co.dto.request.SignUpRequest;
import com.thanh_phoi_co.dto.response.UserResponse;
import com.thanh_phoi_co.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true) // sẽ encode ở service
    @Mapping(target = "lastLogin", ignore = true)
    User toUser(SignUpRequest request);

    UserResponse toUserResponse(User user);
}

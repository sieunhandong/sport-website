package com.thanh_phoi_co.service;

import com.thanh_phoi_co.dto.response.UserResponse;
import com.thanh_phoi_co.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();
    User getByEmail(String email);
    User getByUsername(String username);

    UserResponse getMyInfo();
}

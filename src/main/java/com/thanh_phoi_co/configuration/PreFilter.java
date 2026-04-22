package com.thanh_phoi_co.configuration;

import com.thanh_phoi_co.enums.TokenType;
import com.thanh_phoi_co.model.Token;
import com.thanh_phoi_co.service.JwtService;
import com.thanh_phoi_co.service.TokenService;
import com.thanh_phoi_co.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-------------PreFilter---------------");
        final String authorization = request.getHeader("Authorization");

        if(StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        final String token = authorization.substring("Bearer".length());
        final String userName;
        try {
            userName = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final String roles;
        try {
            roles = jwtService.extractScope(token, TokenType.ACCESS_TOKEN);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //kiem tra hop le cua username va kiem tra xac thuc
        if(StringUtils.isNotEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);
            try {
                if(jwtService.isValid(token, TokenType.ACCESS_TOKEN, userDetails)){

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    for(String role : roles.split(",")){
                        log.info("role: {}",role);
                        authorities.add(new SimpleGrantedAuthority(role.trim()));
                    }
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request,response);
    }

    private void logout(HttpServletRequest request, String userName) {
        // Xóa thông tin token trong session hoặc database
        Token currentToken = tokenService.getByUsername(userName);
        if (currentToken != null) {
            tokenService.delete(currentToken); // Xóa token trong database
        }

        // Thực hiện logout người dùng khỏi SecurityContext
        SecurityContextHolder.clearContext(); // Xóa context hiện tại, đồng nghĩa với việc logout người dùng
        log.info("User {} logged out due to inactivity", userName);
    }
}

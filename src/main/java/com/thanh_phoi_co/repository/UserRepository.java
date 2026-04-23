package com.thanh_phoi_co.repository;

import com.thanh_phoi_co.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query(value = "SELECT ur.roles_name FROM tbl_user_roles ur \n" +
            "JOIN tbl_user u ON u.id = ur.user_id\n" +
            "WHERE u.username = :username",nativeQuery = true)
    List<String> findRolesByUsername(@Param("username") String username);

}

package com.trieunv.secure_web.repository;

import com.trieunv.secure_web.model.Role;
import com.trieunv.secure_web.model.RoleName;
import com.trieunv.secure_web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//package repository chứa interface để sử dụng Hibernate JPA cho việc lưu trữ/lấy lại data từ mysql
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);

}

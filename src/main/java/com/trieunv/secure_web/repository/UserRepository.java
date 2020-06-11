package com.trieunv.secure_web.repository;

import com.trieunv.secure_web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
//    phải gõ đúng tên findByUsername, existsByUsername, existsByEmail, nếu gõ ko đúng thì sẽ báo lỗi
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

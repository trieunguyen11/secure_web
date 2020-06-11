package com.trieunv.secure_web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
//package controller để xác định restAPI cho user đăng kí/ đăng nhập và kiểm tra bảo mật với JWT
public class TestRestAPIs {
//    chungs ta sử dụng @PreAuthorize để quết định khi nào 1 phương thức có thể thực thi hay không
    @GetMapping("/api/test/user")
//    truy nhập bỏi user có USER_ROLE  hoạc ADMIN_ROLE
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {return ">>> User Contents!";}

    @GetMapping("/api/test/pm")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    public String projectManagementAccess() {return ">>> Project Management Board";}

    @GetMapping("/api/test/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {return ">>> Admin Contents!";}

}

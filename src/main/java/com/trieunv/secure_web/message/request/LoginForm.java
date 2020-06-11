package com.trieunv.secure_web.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
//package message xác định khối luongj dũ liệu được truyền tải từ người dùng(Browser/RestClient...) tới RestAPIs
//và trở lại message
public class LoginForm {
    @NotBlank
    @Size(min = 3, max = 60)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

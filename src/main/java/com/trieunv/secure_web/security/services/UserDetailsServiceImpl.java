package com.trieunv.secure_web.security.services;

import com.trieunv.secure_web.model.User;
import com.trieunv.secure_web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        loadUserByUsername là phương thức tìm kiếm  bản ghi từ database user để
//                build vào object UserDetail cho authentication
        // Kiểm tra xem user có tồn tại trong database không?
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found with -> username or email: " + username));
        return UserPrinciple.build(user);
    }
}

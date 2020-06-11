package com.trieunv.secure_web.security;

import com.trieunv.secure_web.security.jwt.JwtAuthEntryPoint;
import com.trieunv.secure_web.security.jwt.JwtAuthTokenFilter;
import com.trieunv.secure_web.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
//spring security cung cấp 1 vài annotation cho việc ktra xác thực trước và sau, lọc khi submit các argument,
//hoặc trả lại giá trị: @PreAuthorize, @PreFilter, @PostAuthorize and @PostFilter.
//để cho phép method security expressions, ta sử dụng annotation EnableGlobalMethodSecurity
//WebSecurityConfig để lọc request
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
//    UserDetailsServiceImpl cũng sẽ lấy userDetail
    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
//        AuthenticationManager: provider do spring cung cấp
//            provider này làm việc tốt vói form login cơ bản hoạc xác thục http cơ bản,
//                nó sẽ xác thục 1 authentication request username/password cơ bản
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        để giúp spring biết khi nào chúng ta muốn tất cả các user phải xác thực,
//                ta implement WebSecurityConfigurerAdapter và cấu hình trong hàm configure(http)
        http.cors().and().csrf().disable().csrf().disable().authorizeRequests().
                antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}

package com.trieunv.secure_web.security.jwt;

import com.trieunv.secure_web.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//JwtAuthTokenFilter validate token sủ dụng JWt Provider

//JwtAuthTokenFilter Có nhiệm vụ kiểm tra request của người dùng trước khi nó tới đích.
//        Nó sẽ lấy Header Authorization ra và kiểm tra xem chuỗi JWT người dùng gửi lên có hợp lệ không.
public class JwtAuthTokenFilter extends OncePerRequestFilter {
//    OncePerRequestFilter: thục thi 1 lần cho mỗi request
//đây là class lọc để đảm bảo cho việc lọc thục thi 1 lần cho mỗi request
    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        doFilterInternal sẽ thục hiện:
//        - lấy JWT token từ header
//        -  validate JWT
//        -  phân tích username từ validates JWT
//        - load data từ table user, sau đó build object "authentication"
//        - set object "authentication" tới Security Context
        try {
            String jwt = getJwt(request);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                // extract user information
                String username = tokenProvider.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // create AuthenticationToken
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                SecurityContextHolder là đối tuọng cơ bản nhất, nơi chúng ta lưu trữ chi tiết bảo mật của ứng dụng
//                Spring security sủ dụng đối tượng Authentication và chúng ta có thể truy vấn đối tuongj authentication
//                        ỏ bất kì đâu trong ứng dụng
//                getContext() sẽ trả lại 1 thể hiện của interface SecurityContext(chứa Authentication và request - thông tin bảo mật đạc biệt)
            }

        } catch (Exception e) {
            logger.error("Can NOT set user authentication -> Message: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
}

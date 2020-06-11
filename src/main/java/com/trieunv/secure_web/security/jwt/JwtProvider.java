package com.trieunv.secure_web.security.jwt;

import com.trieunv.secure_web.security.services.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
//JwtProvider để tạo/validate token
//class này sẽ lấy username từ đối tượng Authentication,
// sau đó build JWT Token với username, Date(), secretKey
@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${grokonez.app.jwtSecret}") // lấy giá trị config tù file application.properties
    private String jwtSecret;

    @Value("86400")
    private int jwtExpiration; //Thời gian có hiệu lực của chuỗi jwt

    public String generateJwtToken(Authentication authentication) {
//        Tạo ra jwt từ thông tin user
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//        chúng ta có thể chứa một principle từ đối tượng authentication
//                principle có thể chuyển đổi thành userDetail để lấy username, password và quyền truy cập(Grant)
//                -> khi authenticating thành công, ta cso thể lấy userDetail từ Authentication
        return Jwts.builder().setSubject((userPrinciple.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message:{}", e);
        } catch (MalformedJwtException e) {
            logger.error("Invlid JWT token -> Message:{}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Mesage:{}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Mesage:{}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Mesage:{}", e);
        }
        return false;
    }

    // Lấy thông tin user từ jwt
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}

package com.trieunv.secure_web.controller;

import com.trieunv.secure_web.message.request.LoginForm;
import com.trieunv.secure_web.message.request.SignUpForm;
import com.trieunv.secure_web.message.response.JwtResponse;
import com.trieunv.secure_web.message.response.ResponseMessage;
import com.trieunv.secure_web.model.Role;
import com.trieunv.secure_web.model.RoleName;
import com.trieunv.secure_web.model.Student;
import com.trieunv.secure_web.model.User;
import com.trieunv.secure_web.repository.RoleRepository;
import com.trieunv.secure_web.repository.StudentRepository;
import com.trieunv.secure_web.repository.UserRepository;
import com.trieunv.secure_web.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    StudentRepository studentRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng.
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already  in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        User user = new User(signupRequest.getName(), signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail -> Cause: User role not find."));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                            .orElseThrow(() -> new RuntimeException("Fail -> Cause: User role not find."));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail -> Cause: User role not find."));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ResponseMessage("User register successfully!"), HttpStatus.OK);
    }

    @GetMapping("students-list")
    public List<Student> allstudents() {
        return studentRepository.findAll();
    }

    @PostMapping("save-student")
    public Student addStudent(@Valid @RequestBody Student student){
        return studentRepository.save(student);
    }

    @GetMapping("student/{id}")
    public List<Student> getStudentById(@PathVariable Integer id){
        Optional<Student> op =  studentRepository.findById(id);
        List<Student> lst = new ArrayList<>();
        lst.add((Student)op.get());
        return lst;
    }

    @PostMapping("update-student/{id}")
    public Student updateStudent(@Valid @RequestBody Student value, @PathVariable Integer id){
        return studentRepository.save(value);
    }

    @DeleteMapping("delete-student/{id}")
    public void deleteStudent(@PathVariable Integer id){
        studentRepository.deleteById(id);
    }
}




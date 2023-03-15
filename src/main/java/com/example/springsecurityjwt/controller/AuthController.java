package com.example.springsecurityjwt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.springsecurityjwt.exception.AppException;
import com.example.springsecurityjwt.model.Role;
import com.example.springsecurityjwt.model.RoleName;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.payload.ApiResponse;
import com.example.springsecurityjwt.payload.JwtAuthenticationResponse;
import com.example.springsecurityjwt.payload.LoginRequest;
import com.example.springsecurityjwt.payload.SignUpRequest;
import com.example.springsecurityjwt.repository.RoleRepository;
import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.security.JwtTokenProvider;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        System.out.println("insignup page");
        // Creating user's account
        System.out.println("insignup page-->0"+signUpRequest.getName()+" "+signUpRequest.getUsername()+" "+signUpRequest.getEmail());
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());
        System.out.println("insignup page-->0"+user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("insignup page-->01"+user);
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        System.out.println("insignup page-->0"+userRole);
        
        user.setRoles(Collections.singleton(userRole));
        
        System.out.println("insignup page-->123");
        User result = userRepository.save(user);
        System.out.println("insignup page-->1"+result);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        System.out.println("insignup page -->2");

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
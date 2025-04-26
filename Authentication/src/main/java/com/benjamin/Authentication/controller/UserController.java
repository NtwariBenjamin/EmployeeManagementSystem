package com.benjamin.Authentication.controller;

import com.benjamin.Authentication.config.JwtService;
import com.benjamin.Authentication.model.LoginRequest;
import com.benjamin.Authentication.model.LoginResponse;
import com.benjamin.Authentication.model.UserRequest;
import com.benjamin.Authentication.model.UserResponse;
import com.benjamin.Authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "User Authentication", description = "Handles user registration, login, and retrieval")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        try {
            log.info("Registering User with User Request: {}", userRequest);
            UserResponse userResponse = userService.registerUser(userRequest);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.error("Error Registering User with Request: {}", userRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates the user and returns a JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .message("Logged In Successfully!")
                    .build();

            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Get user by username", description = "Fetches user details by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        try {
            log.info("Get User by Username {}", username);
            UserResponse userResponse = userService.getUser(username);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.error("User Not Found by Username {}", username, e);
            throw new UsernameNotFoundException("User " + username + " Is Not Found in the System!");
        }
    }

    @GetMapping("/user/id/{userId}")
    @Operation(summary = "Get user by ID", description = "Fetches user details by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        try {
            log.info("Get User by UserId {}", userId);
            UserResponse userResponse = userService.getUserById(userId);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.error("User Not Found by userId {}", userId, e);
            throw new UsernameNotFoundException("User " + userId + " Is Not Found in the System!");
        }
    }
}

package com.benjamin.Authentication.service;

import com.benjamin.Authentication.model.User;
import com.benjamin.Authentication.model.UserRequest;
import com.benjamin.Authentication.model.UserResponse;
import com.benjamin.Authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponse registerUser(UserRequest userRequest) {
        Optional<User> existingUser=userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()){
            return UserResponse.builder()
                    .user(existingUser.get())
                    .message("User Already Exists!")
                    .build();
        }
     User user=User.builder()
             .username(userRequest.getUsername())
             .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
             .role(userRequest.getRole())
             .build();
        userRepository.save(user);
        return UserResponse.builder()
                .user(user)
                .message("User Registered Successfully!")
                .build();
    }

    public UserResponse getUser(String username) {
        Optional<User> optionalUser=userRepository.findByUsername(username);
        if (optionalUser.isEmpty()){
            return UserResponse.builder()
                    .user(null)
                    .message("User Not Found!")
                    .build();
        }
        User user=optionalUser.get();
        return UserResponse.builder()
                .user(user)
                .message("User "+username+" is present in the System")
                .build();
    }

    public UserResponse getUserById(Long userId) {
        Optional<User> optionalUser=userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            return UserResponse.builder()
                    .user(null)
                    .message("User Not Found!")
                    .build();
        }
        User user=optionalUser.get();
        return UserResponse.builder()
                .user(user)
                .message("User "+user.getUsername()+" is present in the System")
                .build();
    }
}

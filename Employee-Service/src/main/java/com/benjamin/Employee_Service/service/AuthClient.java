package com.benjamin.Employee_Service.service;

import com.benjamin.Employee_Service.config.JwtService;
import com.benjamin.Employee_Service.model.user.User;
import com.benjamin.Employee_Service.model.user.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class AuthClient {
    @Value("${authentication.service.url}")
    private String authenticationUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtService jwtService;

    public User getUserDetails(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User Not Found!");
        }
        String url = UriComponentsBuilder
                .fromHttpUrl(authenticationUrl)
                .pathSegment("auth", "user", "id", String.valueOf(userId))
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtService.generateTokenForService());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        log.info("calling Url {}", url);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, UserResponse.class);
        log.info("Raw Response:{}", response.getBody().getUser());
        return response.getBody().getUser();
    }

    public User validateTokenAndGetUser(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

        String url = UriComponentsBuilder
                .fromHttpUrl(authenticationUrl)
                .pathSegment("auth", "validate-token")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        log.info("Validating token with URL: {}", url);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, UserResponse.class);
        
        if (response.getBody() == null || response.getBody().getUser() == null) {
            throw new IllegalArgumentException("Invalid token or user not found");
        }
        
        return response.getBody().getUser();
    }
}

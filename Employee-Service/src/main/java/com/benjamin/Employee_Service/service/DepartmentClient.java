package com.benjamin.Employee_Service.service;

import com.benjamin.Employee_Service.config.JwtService;
import com.benjamin.Employee_Service.model.department.Department;
import com.benjamin.Employee_Service.model.department.DepartmentResponse;
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
public class DepartmentClient {

    @Value("${department.service.url}")
    private String departmentUrl;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;
    public Department getDepartmentDetails(Long departmentId){
        if (departmentId==null){
            throw new IllegalArgumentException("Department Not Found!");
        }
        String url= UriComponentsBuilder
                .fromHttpUrl(departmentUrl)
                .pathSegment("api","department","id", String.valueOf(departmentId))
                .toUriString();
        HttpHeaders headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+jwtService.generateTokenForService());
        HttpEntity<String> entity=new HttpEntity<>(headers);
        log.info("calling Url {}",url);
        ResponseEntity<DepartmentResponse> response=restTemplate.exchange(
            url, HttpMethod.GET,entity, DepartmentResponse.class);
        log.info("Raw Response:{}",response.getBody().getDepartment());
        return response.getBody().getDepartment();
    }
}

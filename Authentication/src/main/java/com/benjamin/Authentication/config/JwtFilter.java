package com.benjamin.Authentication.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.startsWith("/auth/login")
                || path.startsWith("/auth/register");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            String jwtToken=authHeader.substring(7);
            try {
                String subject=jwtService.extractUsername(jwtToken);
                log.info("Subject:{}",subject);
                Claims claims=jwtService.extractAllClaims(jwtToken);
                if ("EmployeeService".equals(subject) && "SERVICE".equals(claims.get("role"))){

                    UsernamePasswordAuthenticationToken authToken=
                            new UsernamePasswordAuthenticationToken(subject,null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }else {
                    UserDetails userDetails=customUserDetailsService.loadUserByUsername(subject);
                    log.info("User Details: {}",userDetails);
                    if (jwtService.validateToken(jwtToken, userDetails)){
                        UsernamePasswordAuthenticationToken authToken=
                                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }catch (Exception e){
                log.error("Invalid Jwt Token",e);
            }
        }
        filterChain.doFilter(request, response);
    }
}

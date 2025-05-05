package com.muffinmanager.api.muffinmanagerapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.Permissions;

@Configuration
public class SecurityConfig {

    @Autowired
    JwtAutenticationFilter jwtAuthFilter;
    @Autowired
    AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(crsfd -> crsfd.disable())
                .authorizeHttpRequests(
                    authRequest ->
                        authRequest
                        .requestMatchers(HttpMethod.GET, "/ws/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/test/get").permitAll()
                        .requestMatchers(HttpMethod.POST, "/manage/**").hasRole(Permissions.manage_data.name())
                        .requestMatchers(HttpMethod.PATCH, "/manage/**").hasRole(Permissions.manage_data.name())
                        .requestMatchers(HttpMethod.DELETE, "/manage/**").hasRole(Permissions.manage_data.name())
                        .requestMatchers(HttpMethod.GET, "/manage/muffin-shape/**").hasAnyRole(Permissions.manage_data.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

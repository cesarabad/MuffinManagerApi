package com.muffinmanager.api.muffinmanagerapi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginResponse;
import com.muffinmanager.api.muffinmanagerapi.model.User.RegisterRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UpdateUserDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.service.auth.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            if (request.getDni() == null || request.getPassword() == null ||
                request.getDni().isEmpty() || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);

        } catch (InternalAuthenticationServiceException iasEx) {
            return ResponseEntity.noContent().build();
        }catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (request.getDni() == null || request.getPassword() == null || 
                request.getName() == null || request.getSecondName() == null){
                return ResponseEntity.badRequest().body(null);
            }
            LoginResponse response = userService.register(request);
            if (response == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<LoginResponse> updateProfile(@RequestBody UpdateUserDto updatedUserDto) {
        try {
            return ResponseEntity.ok(userService.update(updatedUserDto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/all") 
    public ResponseEntity<List<UserSafeDto>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
